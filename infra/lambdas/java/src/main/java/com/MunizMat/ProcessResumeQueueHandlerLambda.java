package com.MunizMat;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcessResumeQueueHandlerLambda implements RequestHandler<SQSEvent, SQSBatchResponse> {
    @Override
    public SQSBatchResponse handleRequest(SQSEvent event, Context context) {
        Environment.read();

        event.getRecords().forEach(this::processMessage);

        return null;
    }

    private void processMessage(SQSEvent.SQSMessage message){
        try {
            String body = message.getBody();

            System.out.println(body);

            ObjectMapper objectMapper = new ObjectMapper();

            ProcessResumeQueueMessage queueMessage = objectMapper.readValue(body, ProcessResumeQueueMessage.class);

            queueMessage.getRecords().forEach(S3ObjectCreatedEventProcessor::processObjectCreatedEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
