package com.myorg.constructs.lambdas;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.nodejs.NodejsFunction;
import software.amazon.awscdk.services.lambda.nodejs.NodejsFunctionProps;
import software.constructs.Construct;

import java.util.Map;

public class GetResumeAnalysisLambda extends Construct {
    private final NodejsFunction lambda;

    public GetResumeAnalysisLambda(final Construct scope, final String id, final GetResumeAnalysisLambda.Props props){
        super(scope, id);

        this.lambda = new NodejsFunction(
                this,
                "GetResumeAnalysisLambda-%s".formatted(props.env()),
                NodejsFunctionProps.builder()
                        .runtime(Runtime.NODEJS_18_X)
                        .entry("../lambdas/nodejs/src/getResumeAnalysisLambda.ts")
                        .depsLockFilePath("../lambdas/nodejs/yarn.lock")
                        .environment(Map.of("TABLE_NAME", props.tableName()))
                        .handler("handler")
                        .functionName("get-resume-analysis-lambda-v2-%s".formatted(props.env().toLowerCase()))
                        .timeout(Duration.seconds(Integer.valueOf(30)))
                        .build()
        );
    }

    public Function getLambda() {
        return lambda;
    }

    public record Props(String env, String tableName){ }
}
