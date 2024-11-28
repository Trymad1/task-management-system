package com.trymad.task_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtErrorResponseWriter extends AbstractJsonErrorResponseWriter {

    private final ErrorResponseSupplyer errorSupplyer;

    public JwtErrorResponseWriter(ObjectMapper mapper, ErrorResponseSupplyer errorSupplyer) {
        super(mapper);
        this.errorSupplyer = errorSupplyer;
    }

    public HttpServletResponse invalidToken(HttpServletRequest request, HttpServletResponse response, JwtException e) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final String message = e.getMessage();

        return writeInResponse(response, errorSupplyer.getResponse(status, request, message).getBody());
    }

    public HttpServletResponse requiredToken(HttpServletRequest request, HttpServletResponse response) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final String message = "Bearer token required";

        return writeInResponse(response, errorSupplyer.getResponse(status, request, message).getBody());
    }

}
