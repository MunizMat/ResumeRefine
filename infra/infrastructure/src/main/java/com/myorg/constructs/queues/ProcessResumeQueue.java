package com.myorg.constructs.queues;

import com.myorg.utils.NameUtils;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueProps;
import software.constructs.Construct;

public class ProcessResumeQueue extends Construct {
    private final Queue queue;

    public ProcessResumeQueue(
            final Construct scope,
            final String id,
            final Props props
    ){
        super(scope, id);

        this.queue = new Queue(
                this,
                NameUtils.generateConstructId("ProcessResumeQueue", props.getEnv()),
                QueueProps.builder()
                        .queueName("resume-refine-process-resume-queue-%s".formatted(props.getEnv()))
                        .visibilityTimeout(Duration.minutes(5))
                        .build()
        );
    }

    public Queue getQueue() {
        return queue;
    }

    public static class Props {
        private final String env;

        public Props(String env){
            this.env = env;
        }

        public String getEnv() {
            return env;
        }
    }
}
