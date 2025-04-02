package com.carlos.charles_api.service.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
