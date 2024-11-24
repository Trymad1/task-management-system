package com.trymad.task_management.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.TaskPriority;
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
    private TaskStatus getStatus(String name) {
        return statusRepository.findByValue(name).orElseThrow(
                () -> new EntityNotFoundException("Status " + name + " is not valid"));
    }

    @Transactional(readOnly = true)
    private TaskPriority getPriority(String name) {
        return priorityRepository.findByValue(name).orElseThrow(
                () -> new EntityNotFoundException("Priority " + name + " is not valid"));
    }

    public Task create(TaskCreateDTO createDto) {
        final Task task = taskMapper.toEntity(createDto);
        final LocalDateTime now = LocalDateTime.now();

        task.setAuthor(userService.get(createDto.authorId()));
        if (createDto.executorId() != null) {
            task.setExecutor(userService.get(createDto.executorId()));
        }

        task.setStatus(this.getStatus(createDto.status()));
        task.setPriority(this.getPriority(createDto.priority()));
        task.setCreatedAt(now);
        task.setChangedAt(now);

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
            task.setStatus(this.getStatus(updateDto.getStatus()));
        }

        if (updateDto.getPriority() != null) {
            task.setPriority(this.getPriority(updateDto.getPriority()));
        }

        task.setChangedAt(LocalDateTime.now());
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
