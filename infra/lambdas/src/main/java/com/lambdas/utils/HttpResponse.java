package com.lambdas.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class HttpResponse {
    private final static Map<String, String> defaultHeaders = Map.of(
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Methods", "OPTIONS,POST,GET,DELETE,PUT,PATCH"
    );

    private final static ObjectMapper mapper = new ObjectMapper();

    public static APIGatewayV2HTTPResponse.APIGatewayV2HTTPResponseBuilder create(){
        return APIGatewayV2HTTPResponse.builder()
                .withHeaders(defaultHeaders);
    }

    public static APIGatewayV2HTTPResponse.APIGatewayV2HTTPResponseBuilder create(Object data){
        String body = "";

        try {
            body = mapper.writeValueAsString(data);
        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }

        return create().withBody(body);
    }
}
