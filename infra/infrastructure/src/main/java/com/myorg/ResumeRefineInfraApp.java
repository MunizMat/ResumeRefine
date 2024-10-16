package com.myorg;

import software.amazon.awscdk.App;

public class ResumeRefineInfraApp {
    public static void main(final String[] args) {
        App app = new App();

        String environment = "PROD";

        new ResumeRefineBackendStack(
                app,
                "ResumeRefineBackendStack-%s".formatted(environment),
                new ResumeRefineBackendStack.Props(environment)
        );

        app.synth();
    }
}

