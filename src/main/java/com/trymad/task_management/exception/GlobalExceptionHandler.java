package com.trymad.task_management.exception;

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.trymad.task_management.util.ErrorResponseSupplyer;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseSupplyer responseSupplyer;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        return responseSupplyer.getResponse(HttpStatus.NOT_FOUND, request, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final StringBuilder messageBuilder = new StringBuilder();
        fieldErrors.stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .forEach(errorField -> messageBuilder.append(errorField + " | "));
        final String message = messageBuilder.substring(0, messageBuilder.length() - 3); // cut | symbol in end, TODO

        return responseSupplyer.getResponse(HttpStatus.BAD_REQUEST, request, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMethodNotSupproted(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        final String message = "Path with current method not found";
        return responseSupplyer.getResponse(HttpStatus.NOT_FOUND, request, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        return responseSupplyer.getResponse(HttpStatus.FORBIDDEN, request, "Access denied");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> usernameNotFound(AuthenticationException e, HttpServletRequest request) {
        return this.handleEntityNotFound(new EntityNotFoundException(e.getMessage()), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> notReadableException(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        return responseSupplyer.getResponse(HttpStatus.BAD_REQUEST, request, "Bad json body");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e,
            HttpServletRequest request) {
        return responseSupplyer.getResponse(HttpStatus.BAD_REQUEST, request, "User already exists");
    }

    // for /refresh endpoint, filter handling exception in JwtErrorResponseWriter.
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(JwtException e,
            HttpServletRequest request) {
        return responseSupplyer.getResponse(HttpStatus.UNAUTHORIZED, request, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
            HttpServletRequest request) {
        final String message = "Excepted " + e.getRequiredType().getSimpleName() + " type in param "
                + e.getParameter().getParameterName();
        return responseSupplyer.getResponse(HttpStatus.BAD_REQUEST, request, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e,
            HttpServletRequest request) throws Exception {
        if (e instanceof Exception)
            throw e;
        return responseSupplyer.getResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                request, "Unexpected server error");
    }

}
