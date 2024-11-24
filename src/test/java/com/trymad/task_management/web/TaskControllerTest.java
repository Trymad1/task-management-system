package com.trymad.task_management.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trymad.task_management.model.Task;
import com.trymad.task_management.service.TaskService;
import com.trymad.task_management.web.controller.TaskController;
import com.trymad.task_management.web.dto.task.TaskCreateDTO;
import com.trymad.task_management.web.dto.task.TaskDTO;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskMapperImpl;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;
    private TaskCreateDTO createDto;
    private TaskDTO taskDto;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Description");
        task.setCreatedAt(LocalDateTime.now());
        task.setChangedAt(LocalDateTime.now());

        createDto = new TaskCreateDTO(1L, 2L, "Task Title", "Task Description", "IN_PROGRESS", "HIGH");

        taskDto = new TaskDTO(1L, 1L, 2L, "Task 1", "Description", "IN_PROGRESS", "HIGH", task.getCreatedAt(),
                task.getChangedAt());
    }

    @Test
    public void givenValidTaskIdShouldReturnTask() throws Exception {
        when(taskService.get(1L)).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDto);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    public void givenValidCreateDtoShouldCreateTask() throws Exception {
        when(taskService.create(createDto)).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDto);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    public void givenValidTaskIdShouldDeleteTask() throws Exception {
        doNothing().when(taskService).delete(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());
    }
}