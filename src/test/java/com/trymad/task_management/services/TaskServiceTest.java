package com.trymad.task_management.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.repository.PriorityRepository;
import com.trymad.task_management.repository.StatusRepository;
import com.trymad.task_management.repository.TaskRepository;
import com.trymad.task_management.service.TaskService;
import com.trymad.task_management.service.UserService;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PriorityRepository priorityRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    private TaskUpdateDTO updateDto;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Task 1 description");
        task.setCreatedAt(LocalDateTime.now());
        task.setChangedAt(LocalDateTime.now());

        updateDto = new TaskUpdateDTO(0L, "Updated Title", "Updated Description", "IN_PROGRESS", "HIGH");
    }

    @Test
    public void givenTaskIdShouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.get(1L);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
    }

    @Test
    public void givenTaskIdShouldThrowNotExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> taskService.get(1L));
        assertEquals("Task with id 1 not found", exception.getMessage());
    }

    @Test
    public void givenUpdateDtoShouldUpdateFields() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(statusRepository.findByValue("IN_PROGRESS")).thenReturn(Optional.of(new TaskStatus()));
        when(priorityRepository.findByValue("HIGH")).thenReturn(Optional.of(new TaskPriority()));
        doAnswer(invocation -> {
            task.setTitle(updateDto.getTitle());
            task.setDescription(updateDto.getDescription());
            return null;
        }).when(taskMapper).updateFromDto(updateDto, task);

        taskService.update(updateDto, 1L);

        verify(taskRepository).save(any(Task.class));
        assertEquals("Updated Title", task.getTitle());
        assertEquals("Updated Description", task.getDescription());
    }

    @Test
    public void givenInvalidUpdateDtoShouldThrowException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(statusRepository.findByValue("INVALID_STATUS")).thenReturn(Optional.empty());
        updateDto.setStatus("INVALID_STATUS");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.update(updateDto, 1L));
        assertEquals("Status INVALID_STATUS is not valid", exception.getMessage());
    }

    @Test
    public void givenUpdateDtoWithInvalidPriorityShouldThrowException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(priorityRepository.findByValue("INVALID_PRIORITY")).thenReturn(Optional.empty());
        when(statusRepository.findByValue(anyString())).thenReturn(Optional.of(new TaskStatus()));
        updateDto.setPriority("INVALID_PRIORITY");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.update(updateDto, 1L));
        assertEquals("Priority INVALID_PRIORITY is not valid", exception.getMessage());
    }

    @Test
    public void givenUpdateDtoWithInvalidTaskShouldThrowException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.update(updateDto, 1L));
        assertEquals("Task with id 1 not found", exception.getMessage());
    }

    @Test
    public void givenUpdateDtoWithNullExetutorShouldUpdateCorrect() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(statusRepository.findByValue(anyString())).thenReturn(Optional.of(new TaskStatus()));
        when(priorityRepository.findByValue(anyString())).thenReturn(Optional.of(new TaskPriority()));

        TaskUpdateDTO updateDtoWithNullExecutorId = new TaskUpdateDTO(0L, "Updated Title", "Updated Description",
                "IN_PROGRESS", "LOW");
        doAnswer(invocation -> {
            task.setTitle(updateDto.getTitle());
            task.setDescription(updateDto.getDescription());
            return null;
        }).when(taskMapper).updateFromDto(updateDtoWithNullExecutorId, task);
        taskService.update(updateDtoWithNullExecutorId, 1L);

        assertNull(task.getExecutor());
        assertEquals("Updated Title", task.getTitle());
        assertEquals("Updated Description", task.getDescription());
    }
}