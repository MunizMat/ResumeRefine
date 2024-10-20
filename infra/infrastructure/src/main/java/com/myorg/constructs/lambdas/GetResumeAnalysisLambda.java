package com.myorg.constructs.lambdas;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.Map;

public class GetResumeAnalysisLambda extends Construct {
    private final Function lambda;

    public GetResumeAnalysisLambda(final Construct scope, final String id, final GetResumeAnalysisLambda.Props props){
        super(scope, id);

        this.lambda = new Function(
                this,
                "GetResumeAnalysisLambda-%s".formatted(props.env()),
                FunctionProps.builder()
                        .runtime(Runtime.JAVA_17)
                        .code(Code.fromAsset("../assets/lambdas.jar"))
                        .environment(Map.of("TABLE_NAME", props.tableName()))
                        .handler("com.lambdas.GetResumeAnalysisLambda")
                        .functionName("get-resume-analysis-lambda-%s".formatted(props.env().toLowerCase()))
                        .timeout(Duration.seconds(Integer.valueOf(30)))
                        .build()
        );
    }

    public Function getLambda() {
        return lambda;
    }

    public record Props(String env, String tableName){ }
}
