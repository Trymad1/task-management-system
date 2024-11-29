package com.trymad.task_management.service;

import java.text.MessageFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.model.TaskStatusEntity;
import com.trymad.task_management.repository.StatusRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
public class TaskStatusService {

    private final StatusRepository statusRepository;

    private final String NOT_VALID_VALUE = "{0} {1} is not valid";

    @Transactional(readOnly = true)
    public TaskStatusEntity get(TaskStatus status) {
        return statusRepository.findByName(status).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(NOT_VALID_VALUE, "Status", status)));
    }
}
