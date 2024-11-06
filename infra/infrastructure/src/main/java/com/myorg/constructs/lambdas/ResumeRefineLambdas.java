package com.myorg.constructs.lambdas;

import com.myorg.constructs.queues.ProcessResumeQueue;
import com.myorg.utils.NameUtils;
import software.constructs.Construct;

public class ResumeRefineLambdas extends Construct {
    private final GetResumeAnalysisLambda getResumeAnalysisLambda;
    private final GetPresignedUrlLambda getPresignedUrlLambda;
    private final ProcessResumeSQSHandlerLambda processResumeSQSHandlerLambda;

    public ResumeRefineLambdas(
            final Construct scope,
            final String id,
            final Props props
    ){
        super(scope, id);

        this.getPresignedUrlLambda = new GetPresignedUrlLambda(
                this,
                NameUtils.generateConstructId("GetPresignedUrlLambda", props.env),
                new GetPresignedUrlLambda.Props(props.env, props.mainBucketName, props.mainTableName)
        );

        this.getResumeAnalysisLambda = new GetResumeAnalysisLambda(
                this,
            NameUtils.generateConstructId("GetResumeAnalysisLambda", props.env),
                new GetResumeAnalysisLambda.Props(props.env, props.mainTableName)
        );

        this.processResumeSQSHandlerLambda = new ProcessResumeSQSHandlerLambda(
                this,
                NameUtils.generateConstructId("ProcessResumeSQSHandlerLambda", props.env),
                new ProcessResumeSQSHandlerLambda.Props(
                        props.mainBucketName,
                        props.processResumeQueue,
                        props.env
                )
        );
    }

    public GetPresignedUrlLambda getGetPresignedUrlLambda() {
        return getPresignedUrlLambda;
    }

    public GetResumeAnalysisLambda getGetResumeAnalysisLambda() {
        return getResumeAnalysisLambda;
    }

    public ProcessResumeSQSHandlerLambda getProcessResumeSQSHandlerLambda() {
        return processResumeSQSHandlerLambda;
    }

    public record Props(String env,
                        String mainBucketName,
                        String mainTableName,
                        ProcessResumeQueue processResumeQueue){ }
}
