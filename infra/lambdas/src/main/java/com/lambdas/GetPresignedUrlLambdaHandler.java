package com.lambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdas.exceptions.ClientErrorException;
import com.lambdas.exceptions.HttpException;
import com.lambdas.exceptions.MissingEnvironmentVariableException;
import com.lambdas.utils.HttpResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GetPresignedUrlLambdaHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context){
        try (S3Presigner presigner = S3Presigner.create()) {
            LambdaLogger logger = context.getLogger();
            String bucketName = System.getenv("BUCKET_NAME");

            if(bucketName.isEmpty())
                throw new MissingEnvironmentVariableException("BUCKET_NAME");

            Map<String, String> queryParameters = event.getQueryStringParameters();

            if (queryParameters == null) throw new ClientErrorException("Missing required query parameters");

            String email = queryParameters.get("email");
            String filename = queryParameters.get("filename");

            if(email.isBlank())
                throw new ClientErrorException("Email is required");

            if(filename.isBlank())
                throw new ClientErrorException("Filename is required");

            String objectId = UUID.randomUUID().toString();

            logger.log("""
                  Client Data: {
                       "email": %s,
                       "filename": %s,
                       "objectId": %s
                  }
                  """.formatted(email, filename, objectId));

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(email + "/" + objectId + "/resume.pdf")
                    .metadata(Map.of("email", email, "filename", filename))
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(request)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            String url = presignedRequest.url().toExternalForm();

            return HttpResponse.create(Map.of("url", url))
                    .withStatusCode(200)
                    .build();
        } catch (HttpException exception) {
          exception.printStackTrace();

            return HttpResponse.create(Map.of("message", exception.getMessage()))
                    .withStatusCode(exception.getStatusCode())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();

            return HttpResponse.create(Map.of("message", "Internal server error"))
                    .withStatusCode(500)
                    .build();
        }

    }
}
