package com.carlos.charles_api.controller.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError() {
        super();
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }
}

@Data
@AllArgsConstructor
class FieldMessage {
    private String fieldName;
    private String message;
}