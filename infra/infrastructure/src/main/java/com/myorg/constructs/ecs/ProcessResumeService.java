package com.myorg.constructs.ecs;

import com.myorg.constructs.queues.ProcessResumeQueue;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.applicationautoscaling.ScalingInterval;
import software.amazon.awscdk.services.ecs.AssetImageProps;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.QueueProcessingFargateService;
import software.amazon.awscdk.services.ecs.patterns.QueueProcessingFargateServiceProps;
import software.constructs.Construct;

import java.util.List;

public class ProcessResumeService extends Construct {
    private final QueueProcessingFargateService service;

    public ProcessResumeService(
            final Construct scope,
            final String id,
            final Props props
    ) {
        super(scope, id);

        this.service = new QueueProcessingFargateService(
                this,
                NameUtils.generateConstructId("ProcessResumeService", props.getEnv()),
                QueueProcessingFargateServiceProps.builder()
                        .queue(props.processResumeQueue.getQueue())
                        .assignPublicIp(true)
                        .disableCpuBasedScaling(true)
                        .minScalingCapacity(0)
                        .maxScalingCapacity(1)
                        .scalingSteps(List.of(
                                ScalingInterval.builder().upper(0).change(-1).build(),
                                ScalingInterval.builder().lower(1).change(+1).build()
                                )
                        )
                        .memoryLimitMiB(512)
                        .cpu(256)
                        .image(
                                ContainerImage.fromAsset(
                                        "../fargate-jobs/ProcessResumeJob",
                                        AssetImageProps.builder().file("Dockerfile").build()
                                )
                        )
                        .build()
        );
    }

    public QueueProcessingFargateService getService() {
        return service;
    }

    public static class Props {
        private final String env;
        private final ProcessResumeQueue processResumeQueue;

        public Props(String env, ProcessResumeQueue processResumeQueue){
            this.env = env;
            this.processResumeQueue = processResumeQueue;
        }

        public String getEnv() {
            return env;
        }
    }
}
