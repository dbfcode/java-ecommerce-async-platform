package com.orderflow.ecommerce.exceptions;

import com.orderflow.ecommerce.dtos.ValidationError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import com.orderflow.ecommerce.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), "Erro de validação", request.getRequestURI());
        ValidationError validationError = new ValidationError(err);
        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            validationError.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(validationError);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Erro na estrutura do JSON: Verifique se você enviou números onde deveria ser texto, ou vice-versa.";

        if (ex.getMessage().contains("Cannot deserialize")) {
            message = "Erro de tipo de dado: Você tentou inserir um valor incompatível com o campo (ex: letras em números ou formato inválido).";
        }

        ErrorResponse err = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                "Você não tem permissão para acessar este recurso.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DuplicateResourceValidationException.class)
    public ResponseEntity<ValidationError> handleDuplicateResource(DuplicateResourceValidationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), ex.getMessage(), request.getRequestURI());
        ValidationError validationError = new ValidationError(err);
        ex.getErrors().forEach(f -> {
            validationError.addError(f.getFieldName(), f.getMessage());
        });

        return ResponseEntity.status(status).body(validationError);
    }
}
