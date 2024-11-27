package com.trymad.task_management.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        final String path = request.getRequestURI();
        final String message = e.getMessage();
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<ErrorResponse>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final StringBuilder messageBuilder = new StringBuilder();
        fieldErrors.stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .forEach(errorField -> messageBuilder.append(errorField + " | "));

        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String path = request.getRequestURI();
        final String message = messageBuilder.toString();
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMethodNotSupproted(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        final String path = request.getRequestURI();
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();
        final String message = "Endpoint with http method + " + method + " not found";

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.FORBIDDEN;
        final String path = request.getRequestURI();
        final String message = e.getMessage();
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<ErrorResponse>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final String path = request.getRequestURI();
        final String message = "Unexpected server error";
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<ErrorResponse>(response, status);
    }

}
