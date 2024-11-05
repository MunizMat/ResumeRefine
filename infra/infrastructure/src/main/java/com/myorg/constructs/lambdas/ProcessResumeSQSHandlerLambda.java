package com.myorg.constructs.lambdas;

import com.myorg.constructs.queues.ProcessResumeQueue;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.Map;

public class ProcessResumeSQSHandlerLambda extends Construct {
    private final Function lambda;

    public ProcessResumeSQSHandlerLambda(
            final Construct scope,
            final String id,
            final Props props
    ){
        super(scope, id);

        Map<String, String> environment = new HashMap<>();

        environment.put("BUCKET_NAME", props.bucketName());
        environment.put("PROCESS_RESUME_QUEUE_URL", props.processResumeQueue().getQueue().getQueueUrl());
        environment.put("OPENAI_API_KEY", SecretValue.secretsManager("OPENAI_API_KEY").unsafeUnwrap());
        environment.put("AWS_JAVA_V1_DISABLE_DEPRECATION_ANNOUNCEMENT", "true");

        this.lambda = new Function(
                this,
                NameUtils.generateConstructId("ProcessResumeSQSHandlerLambda", props.env),
                FunctionProps.builder()
                        .environment(environment)
                        .timeout(Duration.minutes(5))
                        .functionName("process-resume-queue-handler-%s".formatted(props.env))
                        .runtime(Runtime.JAVA_17)
                        .code(Code.fromAsset("../assets/lambdas.jar"))
                        .handler("com.MunizMat.ProcessResumeQueueHandlerLambda")
                        .build()
        );
    }

    public Function getLambda() {
        return lambda;
    }

    public record Props(
            String bucketName,
            ProcessResumeQueue processResumeQueue,
            String env
    ){}
}
