package com.carlos.charles_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldMessage {
    private String fieldName;
    private String message;
}