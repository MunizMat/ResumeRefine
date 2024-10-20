package com.myorg;

import com.myorg.constructs.apigw.ResumeRefineRestApi;
import com.myorg.constructs.dynamo.MainTable;
import com.myorg.constructs.ecs.FargateCluster;
import com.myorg.constructs.ecs.ProcessResumeService;
import com.myorg.constructs.lambdas.GetPresignedUrlLambda;
import com.myorg.constructs.lambdas.GetResumeAnalysisLambda;
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
    private final MainTable mainTable;
    private final ProcessResumeService processResumeService;
    private final FargateCluster fargateCluster;
    private final GetResumeAnalysisLambda getResumeAnalysisLambda;

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


        this.getPresignedUrlLambda = new GetPresignedUrlLambda(
                this,
                NameUtils.generateConstructId("GetPresignedUrlLambda", props.env()),
                new GetPresignedUrlLambda.Props(
                        props.env(),
                        this.mainBucket.getBucket().getBucketName()
                )
        );

        this.getResumeAnalysisLambda = new GetResumeAnalysisLambda(
                this,
                NameUtils.generateConstructId("GetResumeAnalysisLambda", props.env),
                new GetResumeAnalysisLambda.Props(props.env, this.mainTable.getMainTable().getTableName())
        );


        this.restApi = new ResumeRefineRestApi(
                this,
                NameUtils.generateConstructId("RestApi", props.env()),
                new ResumeRefineRestApi.Props(props.env(), this.getPresignedUrlLambda, this.getResumeAnalysisLambda)
        );

        this.mainBucket.getBucket().grantReadWrite(this.getPresignedUrlLambda.getLambda());

        this.processResumeQueue = new ProcessResumeQueue(
                this,
                NameUtils.generateConstructId("ProcessResumeQueue", props.env()),
                new ProcessResumeQueue.Props(props.env())
        );


        this.mainBucket.getBucket().addEventNotification(
                EventType.OBJECT_CREATED,
                new SqsDestination(this.processResumeQueue.getQueue())
        );

        this.fargateCluster = new FargateCluster(
                this,
                NameUtils.generateConstructId("FargateCluster", props.env),
                new FargateCluster.Props(props.env)
        );

        this.processResumeService = new ProcessResumeService(
                this,
                NameUtils.generateConstructId("ProcessResumeService", props.env()),
                new ProcessResumeService.Props(
                        props.env(),
                        this.processResumeQueue,
                        this.mainBucket.getBucket().getBucketName(),
                        this.fargateCluster
                )
        );

        this.mainTable.getMainTable().grantWriteData(this.processResumeService.getService().getTaskDefinition().getTaskRole());
        this.mainBucket.getBucket().grantReadWrite(this.processResumeService.getService().getTaskDefinition().getTaskRole());
        this.mainTable.getMainTable().grantReadData(this.getResumeAnalysisLambda.getLambda());
    }

    public record Props(String env){ }
}
