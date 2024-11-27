package com.trymad.task_management.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Component
public class JwtExceptionResponseWriter {

    private final ObjectMapper mapper;

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

    private HttpServletResponse writeInResponse(HttpServletResponse httpResponse, ErrorResponse responseJson) {
        httpResponse.setContentType("application/json");
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            httpResponse.getWriter().write(mapper.writeValueAsString(responseJson));
        } catch (Exception e) {
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw new RuntimeException("Unexpected exception while writing in exception response");
        }

        return httpResponse;
    }

}
