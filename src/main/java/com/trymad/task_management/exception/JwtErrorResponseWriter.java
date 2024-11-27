package com.trymad.task_management.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtErrorResponseWriter extends AbstractJsonErrorResponseWriter {

    public JwtErrorResponseWriter(ObjectMapper mapper) {
        super(mapper);
    }

    public HttpServletResponse expiredToken(HttpServletRequest request, HttpServletResponse response) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final String path = request.getRequestURI();
        final String message = "Token expired";
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse errorResponse = new ErrorResponse(status.value(), message, path, method, timestamp);
        return writeInResponse(response, errorResponse);
    }

    public HttpServletResponse invalidToken(HttpServletRequest request, HttpServletResponse response) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final String path = request.getRequestURI();
        final String message = "Invalid token signature";
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse errorResponse = new ErrorResponse(status.value(), message, path, method, timestamp);
        return writeInResponse(response, errorResponse);
    }

    public HttpServletResponse requiredToken(HttpServletRequest request, HttpServletResponse response) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final String path = request.getRequestURI();
        final String message = "Unauthorized, bearer token required";
        final Instant timestamp = Instant.now();
        final String method = request.getMethod();

        final ErrorResponse errorResponse = new ErrorResponse(status.value(), message, path, method, timestamp);
        return writeInResponse(response, errorResponse);
    }

}
