package com.trymad.task_management.web.dto.task;

public record TaskParams(Long authorId, Long executorId, String status, String priority) {

}
