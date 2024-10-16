package com.myorg.utils;

public abstract class NameUtils {
    public static String generateConstructId(String name, String environment) {
        return "ResumeRefine-%s-%s".formatted(name, environment);
    }
}
