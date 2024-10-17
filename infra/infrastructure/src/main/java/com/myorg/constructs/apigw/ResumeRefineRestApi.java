package com.myorg.constructs.apigw;

import com.myorg.constructs.lambdas.GetPresignedUrlLambda;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.apigateway.*;
import software.constructs.Construct;

import java.util.List;

public class ResumeRefineRestApi extends Construct {
    private final RestApi restApi;

    public ResumeRefineRestApi(
            final Construct scope,
            final String id,
            Props props
    ){
        super(scope, id);

        this.restApi = new RestApi(
                this,
                NameUtils.generateConstructId("RestApi", props.getEnv()),
                RestApiProps.builder()
                        .build()
        );

        Resource presignedUrlResource = this.restApi.getRoot().addResource(
                "presigned-url",
                ResourceOptions.builder()
                        .defaultCorsPreflightOptions(CorsOptions.builder()
                                .allowMethods(List.of("*"))
                                .allowOrigins(List.of("*"))
                                .build()
                        )
                        .build()
        );

        presignedUrlResource.addMethod(
                "GET",
                new LambdaIntegration(props.getGetPresignedUrlLambda().getLambda())
        );
    }

    public RestApi getRestApi() {
        return restApi;
    }

    public static class Props {
        private final String env;
        private final GetPresignedUrlLambda getPresignedUrlLambda;

        public Props(String env, GetPresignedUrlLambda getPresignedUrlLambda){
            this.getPresignedUrlLambda = getPresignedUrlLambda;
            this.env = env;
        }

        public String getEnv() {
            return env;
        }

        public GetPresignedUrlLambda getGetPresignedUrlLambda() {
            return getPresignedUrlLambda;
        }
    }
}
