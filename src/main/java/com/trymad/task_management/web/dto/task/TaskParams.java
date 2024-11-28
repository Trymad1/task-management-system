package com.trymad.task_management.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskParams(
    
    Long authorId, 

    Long executorId, 

    @Schema(allowableValues = {"NEW", "IN_PROGRESS", "PAUSED", "CANCELED", "COMPLETED"})
    String status, 

    @Schema(allowableValues = {"CRITICAL", "HIGH", "MEDIUM", "LOW", "TRIVIAL"})
    String priority) {

}
