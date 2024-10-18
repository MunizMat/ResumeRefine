package com.MunizMat;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;


/*
* This job is responsible for generating an analysis of resumes uploaded to an S3 bucket.
* The steps involved are:
* 1. Fetch the object in S3
* 2  Get the filename and email associated with the object
* 3. Read the PDF text and generate a PDF image
* 4. Save the PDF image to the S3 bucket
* 5. Get resume analysis from ChatGPT
* */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Process Resume Job");

        try {
            Environment.read();

            String processResumeQueueUrl = System.getenv("PROCESS_RESUME_QUEUE_URL");

            AmazonSQSClient sqsClient = new AmazonSQSClient();

            ReceiveMessageResult result = sqsClient.receiveMessage(processResumeQueueUrl);

            result.getMessages().forEach(MessageProcessor::processMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}