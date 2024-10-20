package com.myorg.constructs.apigw;

import com.myorg.constructs.lambdas.GetPresignedUrlLambda;
import com.myorg.constructs.lambdas.GetResumeAnalysisLambda;
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
                NameUtils.generateConstructId("RestApi", props.env()),
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

        this.getRestApi().getRoot().resourceForPath(
                "/resume/{analysisId}"
        ).addMethod(
                "GET",
                new LambdaIntegration(props.getResumeAnalysisLambda().getLambda())
        );

        presignedUrlResource.addMethod(
                "GET",
                new LambdaIntegration(props.getPresignedUrlLambda().getLambda())
        );
    }

    public RestApi getRestApi() {
        return restApi;
    }


    public record Props(
            String env,
            GetPresignedUrlLambda getPresignedUrlLambda,
            GetResumeAnalysisLambda getResumeAnalysisLambda
    ){ }
}
