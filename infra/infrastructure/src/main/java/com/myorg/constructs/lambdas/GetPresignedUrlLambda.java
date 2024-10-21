package com.myorg.constructs.lambdas;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GetPresignedUrlLambda extends Construct {
    private final Function lambda;

    public GetPresignedUrlLambda(final Construct scope, final String id, final Props props){
        super(scope, id);

        Map<String, String> environment = new HashMap<>();

        environment.put("BUCKET_NAME", props.getMainBucketName());

        this.lambda = new Function(
                this,
                "GetPresignedUrlLambda-%s".formatted(props.getEnv()),
                FunctionProps.builder()
                        .runtime(Runtime.JAVA_17)
                        .code(Code.fromAsset("../assets/lambdas.jar"))
                        .environment(environment)
                        .handler("com.lambdas.GetPresignedUrlLambdaHandler")
                        .functionName("get-presigned-url-lambda-v2-%s".formatted(props.getEnv().toLowerCase()))
                        .timeout(Duration.seconds(Integer.valueOf(30)))
                        .build()
        );
    }

    public Function getLambda() {
        return lambda;
    }

    public static class Props {
        private final String env;
        private final String mainBucketName;

        public Props(String env, String mainBucketName){
            this.env = env;
            this.mainBucketName = mainBucketName;
        }

        public String getEnv() {
            return env;
        }

        public String getMainBucketName() {
            return mainBucketName;
        }
    }
}
