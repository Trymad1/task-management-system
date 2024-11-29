package com.trymad.task_management.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskParams(
    
    @Schema(example = "1")
    Long authorId, 

    @Schema(example = "2")
    Long executorId, 

    @Schema(allowableValues = {"NEW", "IN_PROGRESS", "PAUSED", "CANCELED", "COMPLETED"})
    String status, 

    @Schema(allowableValues = {"CRITICAL", "HIGH", "MEDIUM", "LOW", "TRIVIAL"})
    String priority) {

}
