package com.carlos.charles_api.exceptions;

public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String message) {
        super(message);
    }
}
