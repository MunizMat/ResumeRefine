package com.lambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public class GetPresignedUrlLambdaHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context){
        try (S3Presigner presigner = S3Presigner.create()) {
            LambdaLogger logger = context.getLogger();
            String bucketName = System.getenv("BUCKET_NAME");
            Map<String, String> queryParameters = event.getQueryStringParameters();
            String email = queryParameters.get("email");
            String filename = queryParameters.get("filename");
            String objectId = UUID.randomUUID().toString();

            if(email.isBlank()) throw new RuntimeException("Email is required");
            if(filename.isBlank()) throw new RuntimeException("Filename is required");


            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(email + "/" + objectId + ".pdf")
                    .metadata(Map.of("email", email, "filename", filename))
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(request)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            String url = presignedRequest.url().toExternalForm();

            return APIGatewayV2HTTPResponse
                    .builder()
                    .withStatusCode(200)
                    .withBody(mapper.writeValueAsString(Map.of("url", url)))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            String body = "";

            try {
                body = mapper.writeValueAsString(Map.of("message", e.getMessage()));
            } catch (JsonProcessingException ex){
                ex.printStackTrace();
            }

            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(500)
                    .withBody(body)
                    .build();
        }

    }
}
