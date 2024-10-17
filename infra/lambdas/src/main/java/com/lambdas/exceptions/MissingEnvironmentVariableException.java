package com.lambdas.exceptions;

public class MissingEnvironmentVariableException extends Exception {
    private int statusCode;
    static final String MESSAGE_FORMAT = "%s is not defined";

    public MissingEnvironmentVariableException(String variableName, int statusCode){
        super(MESSAGE_FORMAT.formatted(variableName));
        this.statusCode = statusCode;
    }

    public MissingEnvironmentVariableException(String variableName){
        super(MESSAGE_FORMAT.formatted(variableName));
        this.statusCode = 400;
    }

    public int getStatusCode(){
        return this.statusCode;
    }
}
