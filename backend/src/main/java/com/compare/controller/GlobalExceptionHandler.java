package com.compare.controller;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.compare.controller.api")
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        log.warn("Validation errors: {}", fieldErrors);

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("details", fieldErrors);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> violations = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage(),
                        (a, b) -> a
                ));

        log.warn("Constraint violations: {}", violations);

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("details", violations);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Internal server error");
        body.put("details", Map.of("message", ex.getMessage() != null ? ex.getMessage() : "Unknown error"));
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
