package com.myorg.constructs.lambdas;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.nodejs.NodejsFunction;
import software.amazon.awscdk.services.lambda.nodejs.NodejsFunctionProps;
import software.constructs.Construct;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GetPresignedUrlLambda extends Construct {
    private final NodejsFunction lambda;

    public GetPresignedUrlLambda(final Construct scope, final String id, final Props props){
        super(scope, id);

        Map<String, String> environment = new HashMap<>();

        environment.put("BUCKET_NAME", props.mainBucketName());
        environment.put("TABLE_NAME", props.mainTableName);

        this.lambda = new NodejsFunction(
                this,
                "GetPresignedUrlLambda-%s".formatted(props.env()),
                NodejsFunctionProps.builder()
                        .runtime(Runtime.NODEJS_18_X)
                        .entry("../lambdas/nodejs/src/getPresignedUrlLambda.ts")
                        .environment(environment)
                        .handler("handler")
                        .functionName("get-presigned-url-lambda-v2-%s".formatted(props.env().toLowerCase()))
                        .timeout(Duration.seconds(Integer.valueOf(30)))
                        .depsLockFilePath("../lambdas/nodejs/yarn.lock")
                        .build()
        );
    }

    public Function getLambda() {
        return lambda;
    }

    public record Props(String env, String mainBucketName, String mainTableName) {
    }
}
