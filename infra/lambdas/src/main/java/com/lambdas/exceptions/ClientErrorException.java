package com.lambdas.exceptions;

public class ClientErrorException extends HttpException {
    public ClientErrorException(String message, int statusCode){
        super(message, statusCode);
    }

    public ClientErrorException(String message){
        super(message, 400);
    }
}
