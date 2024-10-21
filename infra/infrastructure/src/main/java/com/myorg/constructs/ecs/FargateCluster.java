package com.myorg.constructs.ecs;

import com.myorg.constructs.ec2.ResumeRefineVPC;
import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ClusterProps;
import software.constructs.Construct;

public class FargateCluster extends Construct  {
    private final Cluster cluster;

    public FargateCluster(
            final Construct scope,
            final String id,
            final Props props
    ) {
        super(scope, id);

        this.cluster = new Cluster(
                this,
                NameUtils.generateConstructId("FargateCluster", props.env),
                ClusterProps.builder()
                        .vpc(props.resumeRefineVPC.getVpc())
                        .clusterName("resume-refine-fargate-cluster-%s".formatted(props.env))
                        .build()
        );
    }

    public Cluster getCluster() {
        return cluster;
    }

    public record Props(String env, ResumeRefineVPC resumeRefineVPC) { }
}
