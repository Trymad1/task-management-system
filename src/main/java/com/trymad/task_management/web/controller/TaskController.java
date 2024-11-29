package com.trymad.task_management.web.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.service.CommentService;
import com.trymad.task_management.service.TaskService;
import com.trymad.task_management.web.dto.comment.CommentCreateDTO;
import com.trymad.task_management.web.dto.comment.CommentDTO;
import com.trymad.task_management.web.dto.comment.CommentMapper;
import com.trymad.task_management.web.dto.task.TaskCreateDTO;
import com.trymad.task_management.web.dto.task.TaskDTO;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskParams;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task controller")

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @Parameters({
            @Parameter(name = "authorId", example = "1"),
            @Parameter(name = "executorId", example = "2")
    })
    @Operation(summary = "Get tasks by filters", description = "Paginate and filter support, return list of tasks")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<TaskDTO> findByFilters(@ParameterObject TaskParams taskParams,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt") Pageable pageable) {
        return taskMapper.toDto(taskService.get(taskParams, pageable));
    }

    @Parameters({
            @Parameter(name = "taskId", example = "1")
    })
    @Operation(summary = "Get task by task id")
    @GetMapping("{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDTO getById(@PathVariable Long taskId) {
        return taskMapper.toDto(taskService.get(taskId));
    }

    @Parameters({
            @Parameter(name = "taskId", example = "1")
    })
    @Operation(summary = "Get comments for task by task id", description = "Paginate and filter support, return list of comments for task")
    @GetMapping("{taskId}/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentDTO> getCommentsForTask(@PathVariable Long taskId, @ParameterObject Pageable pageable) {
        return commentMapper.toDto(commentService.getByTaskId(taskId, pageable));
    }

    @Parameters({
            @Parameter(name = "taskId", example = "1")
    })
    @Operation(summary = "Add comment to task", description = "Only admin or executor task can add new comments to task. The task author is taken from the currently authenticated user")
    @PostMapping("{taskId}/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDTO addCommentsToTask(
            @Valid @RequestBody CommentCreateDTO commentCreateDTO,
            @PathVariable Long taskId) throws AccessDeniedException {
        return commentMapper.toDto(commentService.addComment(commentCreateDTO, taskId));
    }

    @Operation(summary = "Create new task", description = "Only admins can create new task")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO createDTO) {
        return taskMapper.toDto(taskService.create(createDTO));
    }

    @Parameters({
            @Parameter(name = "taskId", example = "1")
    })
    @Operation(summary = "Create new task", 
    description = "Only admins and task executor can change task. Admin can change all field, executor only status")
    @PatchMapping("{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDTO update(
            @RequestBody @Valid TaskUpdateDTO updateDTO,
            @PathVariable Long taskId) throws AccessDeniedException {
        return taskMapper.toDto(taskService.update(updateDTO, taskId));
    }

    @Parameters({
            @Parameter(name = "taskId", example = "1")
    })
    @Operation(summary = "Delete task by id", description = "Only admins can delete the tasks")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }
}
