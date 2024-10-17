package com.myorg;

import com.myorg.constructs.apigw.ResumeRefineRestApi;
import com.myorg.constructs.lambdas.GetPresignedUrlLambda;
import com.myorg.constructs.s3.ResumeRefineMainBucket;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

import java.util.List;

public class ResumeRefineBackendStack extends Stack {
    private final ResumeRefineMainBucket mainBucket;
    private final ResumeRefineRestApi restApi;
    private final GetPresignedUrlLambda getPresignedUrlLambda;

    public ResumeRefineBackendStack(
            final Construct scope,
            final String id,
            Props props
            ) {
        super(scope, id);

        this.mainBucket = new ResumeRefineMainBucket(
                this,
                NameUtils.generateConstructId("MainBucket", props.getEnvironment()),
                new ResumeRefineMainBucket.Props(props.getEnvironment())
        );


        this.getPresignedUrlLambda = new GetPresignedUrlLambda(
                this,
                NameUtils.generateConstructId("GetPresignedUrlLambda", props.getEnvironment()),
                new GetPresignedUrlLambda.Props(
                        props.getEnvironment(),
                        this.mainBucket.getBucket().getBucketName()
                )
        );

        this.restApi = new ResumeRefineRestApi(
                this,
                NameUtils.generateConstructId("RestApi", props.getEnvironment()),
                new ResumeRefineRestApi.Props(props.getEnvironment(), this.getPresignedUrlLambda)
        );

        this.mainBucket.getBucket().grantReadWrite(this.getPresignedUrlLambda.getLambda());

    }

    public static class Props {
        private final String environment;
        public Props(String environment){
            this.environment = environment;
        }

        public String getEnvironment() {
            return environment;
        }
    }
}
