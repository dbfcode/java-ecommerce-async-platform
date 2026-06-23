package com.orderflow.ecommerce.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError{
    private ErrorResponse errorResponse;
    private final List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
