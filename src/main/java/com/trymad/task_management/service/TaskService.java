package com.trymad.task_management.service;

import java.nio.file.AccessDeniedException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.repository.TaskRepository;
import com.trymad.task_management.repository.TaskSpecificationBuilder;
import com.trymad.task_management.web.dto.task.TaskCreateDTO;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskParams;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
@Transactional
public class TaskService {

    private final TaskPriorityService priorityService;
    private final TaskStatusService statusService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final TaskSpecificationBuilder specificationBuilder;

    private final String NOT_FOUND_ID = "{0} with id {1} not found";

    public Slice<Task> get(TaskParams params, Pageable pageable) {

        final TaskStatus status = params.status() == null ? null : TaskStatus.fromString(params.status());
        final TaskPriority priority = params.priority() == null ? null : TaskPriority.fromString(params.priority());

        final Specification<Task> specification = specificationBuilder
                .authorId(params.authorId())
                .executorId(params.executorId())
                .status(status)
                .priority(priority)
                .build();

        final Slice<Task> tasks = taskRepository.findAll(specification, pageable);
        return tasks;
    }

    @Transactional(readOnly = true)
    public Task get(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "Task", id)));
    }

    public void existOrThrow(Long id) {
        if (taskRepository.existsById(id))
            return;
        throw new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "Task", id));
    }

    public Task getExistsReference(Long id) {
        if (taskRepository.existsById(id))
            return taskRepository.getReferenceById(id);
        throw new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "Task", id));
    }

    public Task create(TaskCreateDTO createDto) {
        final Task task = taskMapper.toEntity(createDto);
        final LocalDateTime now = LocalDateTime.now();

        task.setAuthor(userService.get(userService.getCurrentUser().getUsername()));
        if (createDto.executorId() != null) {
            task.setExecutor(userService.get(createDto.executorId()));
        }

        task.setStatus(statusService.get(TaskStatus.fromString(createDto.status())));
        task.setPriority(priorityService.get(TaskPriority.fromString(createDto.priority())));
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        return taskRepository.save(task);
    }

    public Task update(TaskUpdateDTO updateDto, Long id) throws AccessDeniedException {
        final Task task = this.get(id);
        checkPermissions(task, updateDto);

        taskMapper.updateFromDto(updateDto, task);

        if (updateDto.getExecutorId() == null) {
            task.setExecutor(null);
        } else if (updateDto.getExecutorId() != 0L) {
            task.setExecutor(userService.get(updateDto.getExecutorId()));
        }

        if (updateDto.getStatus() != null) {
            task.setStatus(statusService.get(TaskStatus.fromString(updateDto.getStatus())));
        }

        if (updateDto.getPriority() != null) {
            task.setPriority(priorityService.get(TaskPriority.fromString(updateDto.getPriority())));
        }

        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    private void checkPermissions(Task task, TaskUpdateDTO updateDto) throws AccessDeniedException {
        final UserDetails currentUser = userService.getCurrentUser();
        final Set<Role> roles = userService.mapToRoles(currentUser.getAuthorities());
        if (!roles.contains(Role.ADMIN)) {
            if (!task.getExecutor().getMail().equals(currentUser.getUsername())) {
                throw new AccessDeniedException("You can`t update this task");
            }

            updateDto.setDescription(null);
            updateDto.setExecutorId(0L);
            updateDto.setPriority(null);
            updateDto.setTitle(null);
        }
    }

    public void delete(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(MessageFormat.format(NOT_FOUND_ID, "Task", id));
        }
    }
}
