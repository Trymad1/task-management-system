package com.trymad.task_management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskPriorityEntity;
import com.trymad.task_management.model.TaskStatus;
import com.trymad.task_management.model.TaskStatusEntity;
import com.trymad.task_management.model.User;
import com.trymad.task_management.repository.TaskRepository;
import com.trymad.task_management.repository.TaskSpecificationBuilder;
import com.trymad.task_management.web.dto.task.TaskCreateDTO;
import com.trymad.task_management.web.dto.task.TaskMapper;
import com.trymad.task_management.web.dto.task.TaskUpdateDTO;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskPriorityService priorityService;

    @Mock
    private TaskStatusService statusService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserService userService;

    @Mock
    private TaskSpecificationBuilder specificationBuilder;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGet_Success() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGet_Failure() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.get(1L));
    }

    @Test
    void testCreate_Success() {
        TaskCreateDTO createDTO = new TaskCreateDTO(2L, "Test Task", "Task Description", "NEW", "HIGH");
        Task task = new Task();
        task.setId(1L);
        User user = new User();
        user.setMail("dummyMail@mail.com");
        user.setId(1L);

        when(userService.get(anyLong())).thenReturn(user);
        when(userService.get("dummyMail@mail.com")).thenReturn(user);
        when(priorityService.get(any(TaskPriority.class))).thenReturn(new TaskPriorityEntity());
        when(statusService.get(any(TaskStatus.class))).thenReturn(new TaskStatusEntity());
        when(taskMapper.toEntity(createDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(userService.getCurrentUser()).thenReturn(new org.springframework.security.core.userdetails.User(
            "dummyMail@mail.com", "", Set.of(new SimpleGrantedAuthority(Role.ADMIN.toString()))));
        Task result = taskService.create(createDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateAdmin_Success() {
        TaskUpdateDTO updateDTO = new TaskUpdateDTO(2L, "Updated Task", "Updated Description", "IN_PROGRESS", "MEDIUM");
        Task existingTask = new Task();
        existingTask.setId(1L);
        when(userService.get(anyLong())).thenReturn(new User());
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));
        when(priorityService.get(any(TaskPriority.class))).thenReturn(new TaskPriorityEntity());
        when(statusService.get(any(TaskStatus.class))).thenReturn(new TaskStatusEntity());
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(userService.getCurrentUser()).thenReturn(new org.springframework.security.core.userdetails.User(
                "dummyMail@mail.com", "", Set.of(new SimpleGrantedAuthority(Role.ADMIN.toString()))));
        when(userService.mapToRoles(anyCollection())).thenReturn(Set.of(Role.ADMIN));
        doAnswer(invocation -> {
            Task task = invocation.getArgument(1);
            task.setTitle(updateDTO.getTitle());
            task.setDescription(updateDTO.getDescription());
            return null;
        }).when(taskMapper).updateFromDto(updateDTO, existingTask);

        Task result = taskService.update(updateDTO, 1L);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdate_AccessDenied() {
        TaskUpdateDTO updateDTO = new TaskUpdateDTO(2L, "Updated Task", "Updated Description", "IN_PROGRESS", "MEDIUM");
        Task existingTask = new Task();
        existingTask.setId(1L);
        User executor = new User();
        executor.setMail("executor@mail.com");
        existingTask.setExecutor(executor);

        UserDetails currentUser = mock(UserDetails.class);
        when(currentUser.getUsername()).thenReturn("anotherUser@mail.com");
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        assertThrows(AccessDeniedException.class, () -> taskService.update(updateDTO, 1L));
    }

    @Test
    void testDelete_Success() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.delete(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDelete_Failure() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> taskService.delete(1L));
    }
}