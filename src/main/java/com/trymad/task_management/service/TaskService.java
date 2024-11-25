package com.trymad.task_management.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.TaskStatusEntity;
import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskPriorityEntity;
import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.repository.PriorityRepository;
import com.trymad.task_management.repository.StatusRepository;
import com.trymad.task_management.repository.TaskRepository;
import com.trymad.task_management.web.dto.task.TaskCreateDTO;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
@Transactional
public class TaskService {

    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Task get(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    private TaskStatusEntity getStatus(TaskStatus status) {
        return statusRepository.findByValue(status).orElseThrow(
                () -> new EntityNotFoundException("Status " + status + " is not valid"));
    }

    @Transactional(readOnly = true)
    private TaskPriorityEntity getPriority(TaskPriority priority) {
        return priorityRepository.findByValue(priority).orElseThrow(
                () -> new EntityNotFoundException("Priority " + priority + " is not valid"));
    }

    public Task create(TaskCreateDTO createDto) {
        final Task task = taskMapper.toEntity(createDto);
        final LocalDateTime now = LocalDateTime.now();

        task.setAuthor(userService.get(createDto.authorId()));
        if (createDto.executorId() != null) {
            task.setExecutor(userService.get(createDto.executorId()));
        }

        task.setStatus(this.getStatus(TaskStatus.fromString(createDto.status())));
        task.setPriority(this.getPriority(TaskPriority.fromString(createDto.priority())));
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        return taskRepository.save(task);
    }

    public Task update(TaskUpdateDTO updateDto, Long id) {
        final Task task = this.get(id);
        taskMapper.updateFromDto(updateDto, task);

        if (updateDto.getExecutorId() == null) {
            task.setExecutor(null);
        } else if (updateDto.getExecutorId() != 0L) {
            task.setExecutor(userService.get(updateDto.getExecutorId()));
        }

        if (updateDto.getStatus() != null) {
            task.setStatus(this.getStatus(TaskStatus.fromString(updateDto.getStatus())));
        }

        if (updateDto.getPriority() != null) {
            task.setPriority(this.getPriority(TaskPriority.fromString(updateDto.getPriority())));
        }

        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
    }
}
