package com.orderflow.ecommerce.exceptions;

import com.orderflow.ecommerce.dtos.FieldMessage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DuplicateResourceValidationException extends RuntimeException {

    private final List<FieldMessage> errors = new ArrayList<>();

    public DuplicateResourceValidationException(List<FieldMessage> errors, String message) {
        super(message);
        this.errors.addAll(errors);
    }
}
