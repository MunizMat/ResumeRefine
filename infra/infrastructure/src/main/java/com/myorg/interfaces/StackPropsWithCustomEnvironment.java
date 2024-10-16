package com.myorg.interfaces;

import software.amazon.awscdk.StackProps;

public interface StackPropsWithCustomEnvironment extends StackProps {
    String getEnvironment();
}
