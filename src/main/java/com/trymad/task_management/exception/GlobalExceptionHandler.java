package com.trymad.task_management.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.trymad.task_management.security.UserAlreadyExistsException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        return this.buildResponse(HttpStatus.NOT_FOUND, request, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final StringBuilder messageBuilder = new StringBuilder();
        fieldErrors.stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .forEach(errorField -> messageBuilder.append(errorField + " | "));

        return this.buildResponse(HttpStatus.BAD_REQUEST, request, messageBuilder.toString());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMethodNotSupproted(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        final String message = "Path with current method not found";
        return this.buildResponse(HttpStatus.NOT_FOUND, request, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        return this.buildResponse(HttpStatus.FORBIDDEN, request, "Access denied");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> usernameNotFound(AuthenticationException e, HttpServletRequest request) {
        return this.handleEntityNotFound(new EntityNotFoundException(e.getMessage()), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> notReadableException(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, request, "Bad json body");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e,
            HttpServletRequest request) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, request, "User already exists");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, HttpServletRequest request, String message) {
        final String path = request.getRequestURI();
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse response = new ErrorResponse(status.value(), message, path, method, timestamp);
        return new ResponseEntity<ErrorResponse>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e,
            HttpServletRequest request) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, request, "Unexpected server error");
    }

}
