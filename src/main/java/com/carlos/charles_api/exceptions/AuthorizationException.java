package com.carlos.charles_api.exceptions;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}
