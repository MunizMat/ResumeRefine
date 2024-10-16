package com.myorg.constructs.apigw;

import com.myorg.utils.NameUtils;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.RestApiProps;
import software.constructs.Construct;

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

    }

    public RestApi getRestApi() {
        return restApi;
    }

    public static class Props {
        private final String env;

        public Props(String env){
            this.env = env;
        }

        public String getEnv() {
            return env;
        }
    }
}
