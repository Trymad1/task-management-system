package com.trymad.task_management.exception;

import java.time.Instant;

public record ErrorResponse(

    int status,
    String message,
    String path,
    String method,
    Instant timestamp

) { }
