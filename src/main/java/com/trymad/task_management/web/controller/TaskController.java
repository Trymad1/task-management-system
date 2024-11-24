package com.trymad.task_management.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trymad.task_management.service.TaskService;
import com.trymad.task_management.web.dto.task.TaskCreateDTO;
import com.trymad.task_management.web.dto.task.TaskDTO;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDTO getById(@PathVariable Long id) {
        return taskMapper.toDto(taskService.get(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskDTO create(@RequestBody TaskCreateDTO createDTO) {
        return taskMapper.toDto(taskService.create(createDTO));
    }

    @PatchMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDTO update(@RequestBody TaskUpdateDTO updateDTO, @PathVariable Long id) {
        return taskMapper.toDto(taskService.update(updateDTO, id));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

}
