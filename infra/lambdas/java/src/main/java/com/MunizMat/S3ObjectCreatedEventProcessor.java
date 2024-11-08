package com.MunizMat;

import com.MunizMat.models.ResumeAnalysis;
import com.MunizMat.utils.Constants;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.stefanbratanov.jvm.openai.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.joda.time.DateTime;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class S3ObjectCreatedEventProcessor {

    public static void processObjectCreatedEvent(ProcessResumeQueueMessage.Record record){
        try (S3Client client = S3Client.create()) {
            String encodedCreatedObjectKey = record.getS3().getObject().getKey();
            String createdObjectKey = URLDecoder.decode(encodedCreatedObjectKey, StandardCharsets.UTF_8);
            String analysisId = createdObjectKey.split("/")[1];
            String ipAddress = record.getRequestParameters().getSourceIPAddress();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(Environment.BUCKET_NAME)
                    .key(createdObjectKey)
                    .build();

            ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(getObjectRequest);

            Map<String, String> metadata = responseInputStream.response().metadata();

            String filename = metadata.get("filename");
            String email = metadata.get("email");

            if(filename.isBlank())
                throw new RuntimeException("Filename is missing from metadata");

            if(email.isBlank())
                throw new RuntimeException("Email is missing from metadata");

            System.out.printf("""
                    Client data: {
                        "email": "%s",
                        "filename": "%s",
                        "ipAddress": "%s",
                        "analysisId": "%s"
                    }
                    %n""", email, filename, ipAddress, analysisId);

            PDDocument document = PDDocument.load(responseInputStream);
            PDFTextStripper textStripper = new PDFTextStripper();

            String parsedPdfText = textStripper.getText(document);

            uploadPdfPreviewImage(document, client, createdObjectKey.replace(".pdf", ".jpg"));

            document.close();

            String output = getChatGptResumeAnalysis(parsedPdfText);

            System.out.printf("Chat GPT Resume Analysis: %s", output);

            ObjectMapper mapper =  new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);


            ResumeFeedback resumeFeedback = mapper.readValue(output, ResumeFeedback.class);


            writeResumeAnalysisToDb(email, filename, analysisId, ipAddress ,resumeFeedback);

            sendResumeAnalysisEmail(email, "https://resume-refine.com/analysis/%s".formatted(analysisId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void uploadPdfPreviewImage(PDDocument document, S3Client client, String key) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI (0, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(Environment.BUCKET_NAME)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        client.putObject(putObjectRequest, RequestBody.fromBytes(outputStream.toByteArray()));
    }

    private static String getChatGptResumeAnalysis(String parsedPdfText){
        OpenAI openAI = OpenAI.newBuilder(Environment.OPENAI_API_KEY).build();

        String prompt = Constants.RESUME_ANALYSIS_PROMPT + "\n" + parsedPdfText;

        ChatClient chatClient = openAI.chatClient();
        CreateChatCompletionRequest createChatCompletionRequest = CreateChatCompletionRequest.newBuilder()
                .model(OpenAIModel.GPT_4o_MINI)
                .message(ChatMessage.userMessage(prompt))
                .responseFormat(ResponseFormat.jsonSchema(JsonSchema.newBuilder()
                                .name("ResumeAnalysisSchema")
                                .schema(Constants.getSchemaAsMap())
                        .build()))
                .build();

        ChatCompletion chatCompletion = chatClient.createChatCompletion(createChatCompletionRequest);

        return chatCompletion.choices().get(0).message().content();
    }

    private static void writeResumeAnalysisToDb(
            String email,
            String filename,
            String analysisId,
            String ipAddress,
            ResumeFeedback resumeFeedback) {
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient();
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);

        // Set TTL to 5 days
        long timeToLive = new DateTime().plusDays(5).getMillis() / 1000;

        ResumeAnalysis resumeAnalysis = new ResumeAnalysis(
                "USER_IP#%s".formatted(ipAddress),
                "ANALYSIS_ID#%s".formatted(analysisId),
                email,
                analysisId,
                filename,
                DateTime.now().getMillis(),
                resumeFeedback,
                ipAddress,
                timeToLive
        );

        dynamoDBMapper.save(
                resumeAnalysis,
                DynamoDBMapperConfig.builder()
                        .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(
                                Environment.TABLE_NAME
                        ))
                        .build()
        );
    }

    private static void sendResumeAnalysisEmail(String email, String resumeAnalysisUrl){
        AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient();

        SendEmailRequest sendEmailRequest = new SendEmailRequest(
                "noreply@resume-refine.com",
                new Destination(List.of(email)),
                new Message(
                        new Content("Resume Refine Analysis"),
                        new Body(
                                new Content("Your resume analysis can be found at: %s. This link will expire after 5 days".formatted(resumeAnalysisUrl))
                        )
                )
        );

        sesClient.sendEmail(sendEmailRequest);
    }
}
