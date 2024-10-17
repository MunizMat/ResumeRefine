package com.lambdas.exceptions;

public class HttpException extends Exception {
    private final int statusCode;

    public HttpException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
