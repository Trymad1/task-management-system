package com.trymad.task_management.service;

import java.text.MessageFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskPriorityEntity;
import com.trymad.task_management.repository.PriorityRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskPriorityService {

    private final PriorityRepository priorityRepository;

    private final String NOT_VALID_VALUE = "{0} {1} is not valid";

    @Transactional(readOnly = true)
    public TaskPriorityEntity get(TaskPriority priority) {
        return priorityRepository.findByName(priority).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(NOT_VALID_VALUE, "Priority", priority)));
    }
}
