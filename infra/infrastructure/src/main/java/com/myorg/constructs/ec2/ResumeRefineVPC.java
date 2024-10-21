package com.myorg.constructs.ec2;

import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

import java.util.List;

public class ResumeRefineVPC extends Construct {
    private final Vpc vpc;
    private final SecurityGroup securityGroup;

    public ResumeRefineVPC(
            final Construct scope,
            final String id,
            final Props props
    ){
        super(scope, id);

        this.vpc = new Vpc(
                this,
                NameUtils.generateConstructId("Vpc", props.env),
                VpcProps.builder()
                        .natGateways(0)
                        .restrictDefaultSecurityGroup(false)
                        .subnetConfiguration(List.of(
                                SubnetConfiguration.builder()
                                        .name("resume-refine-public-subnet-%s".formatted(props.env.toLowerCase()))
                                        .subnetType(SubnetType.PUBLIC)
                                        .build()
                        ))
                        .vpcName("resume-refine-vpc-%s".formatted(props.env))
                        .build()
        );

        this.securityGroup = new SecurityGroup(
                this,
                NameUtils.generateConstructId("SecurityGroup", props.env),
                SecurityGroupProps.builder()
                        .vpc(this.vpc)
                        .securityGroupName("resume-refine-security-group-%s".formatted(props.env))
                        .build()
        );

        this.securityGroup.addEgressRule(Peer.anyIpv4(), Port.allTcp());
        this.securityGroup.addIngressRule(Peer.anyIpv4(), Port.allTcp());
    }

    public Vpc getVpc() {
        return vpc;
    }

    public record Props(String env) { }
}
