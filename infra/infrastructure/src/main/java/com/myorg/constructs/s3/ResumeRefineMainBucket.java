package com.myorg.constructs.s3;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.s3.*;
import software.constructs.Construct;

import java.util.List;

public class ResumeRefineMainBucket extends Construct  {
    private final Bucket bucket;

    public ResumeRefineMainBucket(final Construct scope, final String id, final Props props){
        super(scope, id);

        this.bucket = new Bucket(
                this,
                "ResumeRefineMainBucket-%s".formatted(props.getEnv()),
                BucketProps.builder()
                        .bucketName("resume-refine-main-bucket-%s".formatted(props.getEnv().toLowerCase()))
                        .cors(List.of(
                                CorsRule.builder()
                                        .allowedHeaders(List.of("*"))
                                        .allowedMethods(List.of(HttpMethods.POST, HttpMethods.PUT))
                                        .allowedOrigins(List.of("*"))
                                        .build())
                        )
                        .autoDeleteObjects(true)
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .objectOwnership(ObjectOwnership.OBJECT_WRITER)
                        .blockPublicAccess(
                                new BlockPublicAccess(
                                        BlockPublicAccessOptions.builder()
                                                .blockPublicAcls(false)
                                                .blockPublicPolicy(false)
                                                .ignorePublicAcls(false)
                                                .restrictPublicBuckets(false)
                                                .build()
                                )
                        )
                        .accessControl(BucketAccessControl.PUBLIC_READ_WRITE)
                        .lifecycleRules(
                                List.of(
                                        LifecycleRule.builder()
                                                .expiration(Duration.days(2))
                                        .build()
                                )
                        )
                        .build()
        );
    }

    public Bucket getBucket(){
        return this.bucket;
    }

    public static class Props {
        private String env;

        public Props(String env){
            this.env = env;
        }

        public String getEnv() {
            return env;
        }
    }
}
