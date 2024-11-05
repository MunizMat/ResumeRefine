package com.myorg;

import com.myorg.constructs.apigw.ResumeRefineRestApi;
import com.myorg.constructs.dynamo.MainTable;
import com.myorg.constructs.lambdas.ResumeRefineLambdas;
import com.myorg.constructs.queues.ProcessResumeQueue;
import com.myorg.constructs.s3.ResumeRefineMainBucket;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.NotificationKeyFilter;
import software.amazon.awscdk.services.s3.notifications.SqsDestination;
import software.constructs.Construct;

import java.util.List;

public class ResumeRefineBackendStack extends Stack {
    private final ResumeRefineMainBucket mainBucket;
    private final ResumeRefineRestApi restApi;
    private final ResumeRefineLambdas resumeRefineLambdas;
    private final ProcessResumeQueue processResumeQueue;
    private final MainTable mainTable;

    public ResumeRefineBackendStack(
            final Construct scope,
            final String id,
            Props props
            ) {
        super(scope, id);

        this.mainBucket = new ResumeRefineMainBucket(
                this,
                NameUtils.generateConstructId("MainBucket", props.env()),
                new ResumeRefineMainBucket.Props(props.env())
        );

        this.mainTable = new MainTable(
                this,
                NameUtils.generateConstructId("MainTable", props.env()),
                new MainTable.Props(props.env())
        );

        this.processResumeQueue = new ProcessResumeQueue(
                this,
                NameUtils.generateConstructId("ProcessResumeQueue", props.env()),
                new ProcessResumeQueue.Props(props.env())
        );


        this.resumeRefineLambdas = new ResumeRefineLambdas(
                this,
                NameUtils.generateConstructId("ResumeRefineLambdas", props.env),
                new ResumeRefineLambdas.Props(
                        props.env,
                        this.mainBucket.getBucket().getBucketName(),
                        this.mainTable.getMainTable().getTableName(),
                        this.processResumeQueue
                )
        );


        this.restApi = new ResumeRefineRestApi(
                this,
                NameUtils.generateConstructId("RestApi", props.env()),
                new ResumeRefineRestApi.Props(props.env(), this.resumeRefineLambdas.getGetPresignedUrlLambda(), this.resumeRefineLambdas.getGetResumeAnalysisLambda())
        );

        this.mainBucket.getBucket().grantReadWrite(this.resumeRefineLambdas.getGetPresignedUrlLambda().getLambda());


        this.mainBucket.getBucket().addEventNotification(
                EventType.OBJECT_CREATED,
                new SqsDestination(this.processResumeQueue.getQueue()),
                NotificationKeyFilter.builder()
                        .suffix(".pdf")
                        .build()
        );


        this.mainTable.getMainTable().grantReadData(this.resumeRefineLambdas.getGetResumeAnalysisLambda().getLambda());
        this.mainTable.getMainTable().grantWriteData(this.resumeRefineLambdas.getProcessResumeSQSHandlerLambda().getLambda());

        this.resumeRefineLambdas.getProcessResumeSQSHandlerLambda().getLambda().addEventSource(
                new SqsEventSource(this.processResumeQueue.getQueue())
        );

        this.resumeRefineLambdas.getProcessResumeSQSHandlerLambda().getLambda()
                .addToRolePolicy(new PolicyStatement(
                        PolicyStatementProps.builder()
                                .actions(List.of("ses:SendEmail"))
                                .effect(Effect.ALLOW)
                                .resources(List.of("*"))
                                .build()
                ));

        this.mainBucket.getBucket().grantReadWrite(this.resumeRefineLambdas.getProcessResumeSQSHandlerLambda().getLambda());
        this.mainBucket.getBucket().grantPutAcl(this.resumeRefineLambdas.getProcessResumeSQSHandlerLambda().getLambda());
    }

    public record Props(String env){ }
}
