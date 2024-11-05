package com.myorg.constructs.ecs;

import com.myorg.constructs.queues.ProcessResumeQueue;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.services.applicationautoscaling.ScalingInterval;
import software.amazon.awscdk.services.ecs.AssetImageProps;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.patterns.QueueProcessingFargateService;
import software.amazon.awscdk.services.ecs.patterns.QueueProcessingFargateServiceProps;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.LogGroupProps;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessResumeService extends Construct {
    private final QueueProcessingFargateService service;

    public ProcessResumeService(
            final Construct scope,
            final String id,
            final Props props
    ) {
        super(scope, id);

        Map<String, String> environment = new HashMap<>();

        environment.put("BUCKET_NAME", props.bucketName());
        environment.put("PROCESS_RESUME_QUEUE_URL", props.processResumeQueue().getQueue().getQueueUrl());
        environment.put("OPENAI_API_KEY", SecretValue.secretsManager("OPENAI_API_KEY").unsafeUnwrap());
        environment.put("AWS_JAVA_V1_DISABLE_DEPRECATION_ANNOUNCEMENT", "true");

        this.service = new QueueProcessingFargateService(
                this,
                NameUtils.generateConstructId("ProcessResumeService", props.env()),
                QueueProcessingFargateServiceProps.builder()
                        .cluster(props.fargateCluster().getCluster())
                        .queue(props.processResumeQueue().getQueue())
                        .assignPublicIp(true)
                        .disableCpuBasedScaling(true)
                        .minScalingCapacity(0)
                        .maxScalingCapacity(1)
                        .logDriver(LogDriver.awsLogs(
                                AwsLogDriverProps.builder()
                                        .logRetention(RetentionDays.THREE_DAYS)
                                        .streamPrefix("resumerefine")
                                        .build()
                        ))
                        .scalingSteps(List.of(
                                ScalingInterval.builder().upper(0).change(-1).build(),
                                ScalingInterval.builder().lower(1).change(+1).build()
                                )
                        )
                        .memoryLimitMiB(1024)
                        .cpu(512)
                        .image(
                                ContainerImage.fromAsset(
                                        "../fargate-jobs/process-resume-job",
                                        AssetImageProps.builder().file("Dockerfile").build()
                                )
                        )
                        .environment(environment)
                        .build()
        );
    }

    public QueueProcessingFargateService getService() {
        return service;
    }

    public record Props(
            String env,
            ProcessResumeQueue processResumeQueue,
            String bucketName,
            FargateCluster fargateCluster
    ) { }
}
