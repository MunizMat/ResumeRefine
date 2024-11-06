package com.myorg.constructs.dynamo;

import com.myorg.utils.NameUtils;

import software.amazon.awscdk.services.dynamodb.*;
import software.constructs.Construct;

public class MainTable extends Construct {
    private Table mainTable;

    public MainTable(
            final Construct scope,
            final String id,
            final Props props
    ){
        super(scope, id);

        this.mainTable = new Table(
                this,
                NameUtils.generateConstructId("MainTable", props.env()),
                TableProps.builder()
                        .tableName("resume-refine-main-table-%s".formatted(props.env().toLowerCase()))
                        .timeToLiveAttribute("timeToLive")
                        .partitionKey(Attribute.builder()
                                .name("partitionKey")
                                .type(AttributeType.STRING)
                                .build())
                        .sortKey(Attribute.builder()
                                .name("sortKey")
                                .type(AttributeType.STRING)
                                .build())
                        .build()

        );

        this.mainTable.addGlobalSecondaryIndex(
                GlobalSecondaryIndexProps.builder()
                        .indexName("sortKey-partitionKey-gsi")
                        .partitionKey(Attribute.builder()
                                .type(AttributeType.STRING)
                                .name("sortKey")
                                .build())
                        .sortKey(Attribute.builder()
                                .type(AttributeType.STRING)
                                .name("partitionKey")
                                .build())
                        .build()
        );
    }

    public Table getMainTable() {
        return mainTable;
    }

    public record Props(String env){ }
}
