package com.myorg;

import com.myorg.constructs.apigw.ResumeRefineRestApi;
import com.myorg.constructs.lambdas.GetPresignedUrlLambda;
import com.myorg.constructs.queues.ProcessResumeQueue;
import com.myorg.constructs.s3.ResumeRefineMainBucket;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.NotificationKeyFilter;
import software.amazon.awscdk.services.s3.notifications.SqsDestination;
import software.constructs.Construct;

import java.util.List;

public class ResumeRefineBackendStack extends Stack {
    private final ResumeRefineMainBucket mainBucket;
    private final ResumeRefineRestApi restApi;
    private final GetPresignedUrlLambda getPresignedUrlLambda;
    private final ProcessResumeQueue processResumeQueue;

    public ResumeRefineBackendStack(
            final Construct scope,
            final String id,
            Props props
            ) {
        super(scope, id);

        this.mainBucket = new ResumeRefineMainBucket(
                this,
                NameUtils.generateConstructId("MainBucket", props.getEnvironment()),
                new ResumeRefineMainBucket.Props(props.getEnvironment())
        );


        this.getPresignedUrlLambda = new GetPresignedUrlLambda(
                this,
                NameUtils.generateConstructId("GetPresignedUrlLambda", props.getEnvironment()),
                new GetPresignedUrlLambda.Props(
                        props.getEnvironment(),
                        this.mainBucket.getBucket().getBucketName()
                )
        );

        this.restApi = new ResumeRefineRestApi(
                this,
                NameUtils.generateConstructId("RestApi", props.getEnvironment()),
                new ResumeRefineRestApi.Props(props.getEnvironment(), this.getPresignedUrlLambda)
        );

        this.mainBucket.getBucket().grantReadWrite(this.getPresignedUrlLambda.getLambda());

        this.processResumeQueue = new ProcessResumeQueue(
                this,
                NameUtils.generateConstructId("ProcessResumeQueue", props.getEnvironment()),
                new ProcessResumeQueue.Props(props.getEnvironment())
        );

        // Attacth a policy allowing the S3 to send messages to the queue
//        this.mainBucket.getBucket().addToResourcePolicy(
//                new PolicyStatement(PolicyStatementProps.builder()
//                        .principals(List.of(new ServicePrincipal("s3.amazonaws.com")))
//                        .actions(List.of("sqs:SendMessage"))
//                        .effect(Effect.ALLOW)
//                        .resources(List.of(this.processResumeQueue.getQueue().getQueueArn()))
//                        .build())
//        );

        this.mainBucket.getBucket().addEventNotification(
                EventType.OBJECT_CREATED,
                new SqsDestination(this.processResumeQueue.getQueue())
        );
    }

    public static class Props {
        private final String environment;
        public Props(String environment){
            this.environment = environment;
        }

        public String getEnvironment() {
            return environment;
        }
    }
}
