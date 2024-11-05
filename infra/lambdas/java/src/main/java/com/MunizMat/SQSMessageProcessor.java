package com.MunizMat;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SQSMessageProcessor {
    public static void processMessage(SQSEvent.SQSMessage message) {
       try {
           String body = message.getBody();

           System.out.println(body);

           ObjectMapper objectMapper = new ObjectMapper();

           ProcessResumeQueueMessage queueMessage = objectMapper.readValue(body, ProcessResumeQueueMessage.class);

           queueMessage.getRecords().forEach(record -> {
               S3ObjectCreatedEventProcessor.processObjectCreatedEvent(record);

               deleteMessage(message.getReceiptHandle());
           });
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public static void deleteMessage(String receiptHandle) {
        AmazonSQSClient client = new AmazonSQSClient();

        DeleteMessageRequest request = new DeleteMessageRequest(Environment.PROCESS_RESUME_QUEUE_URL, receiptHandle);

        client.deleteMessage(request);
    }
}
