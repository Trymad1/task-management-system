package com.trymad.task_management.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trymad.task_management.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Component
public abstract class AbstractJsonErrorResponseWriter {

    private final ObjectMapper mapper;

     protected HttpServletResponse writeInResponse(HttpServletResponse httpResponse, ErrorResponse responseJson) {
        httpResponse.setContentType("application/json");
        httpResponse.setStatus(responseJson.status());
        try {
            httpResponse.getWriter().write(mapper.writeValueAsString(responseJson));
        } catch (Exception e) {
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw new RuntimeException("Unexpected exception while writing in exception response");
        }

        return httpResponse;
    }   
}
