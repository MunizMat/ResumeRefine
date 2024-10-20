package com.lambdas;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GetResumeAnalysisLambda implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context){
        try {
            LambdaLogger logger = context.getLogger();
            String tableName = System.getenv("TABLE_NAME");

            if(tableName.isEmpty())
                throw new MissingEnvironmentVariableException("TABLE_NAME");

            Map<String, String> pathParameters = event.getPathParameters();
            Map<String, String> queryParameters = event.getQueryStringParameters();

            if(pathParameters == null)
                throw new ClientErrorException("analysisId path parameter is required");

            if(queryParameters == null)
                throw new ClientErrorException("email query parameter is required");

            String analysisId = pathParameters.get("analysisId");
            String email = queryParameters.get("email");

            if(analysisId.isBlank())
                throw new ClientErrorException("analysisId is required");

            if(email.isBlank())
                throw new ClientErrorException("email is required");

            AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient();

            Map<String, AttributeValue> key = new HashMap<>();

            key.put("partitionKey", new AttributeValue().withS("USER#%s".formatted(email)));
            key.put("sortKey", new AttributeValue().withS("ANALYSIS_ID#%s".formatted(analysisId)));

            GetItemRequest request = new GetItemRequest(tableName, key);

            GetItemResult result = dynamoDBClient.getItem(request);

            Item item = ItemUtils.toItem(result.getItem());

            return HttpResponse.create(item)
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
