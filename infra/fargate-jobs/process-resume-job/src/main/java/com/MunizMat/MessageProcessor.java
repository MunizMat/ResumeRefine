package com.MunizMat;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageProcessor {
    public static void processMessage(Message message) {
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
