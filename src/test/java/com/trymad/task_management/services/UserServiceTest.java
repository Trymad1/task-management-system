package com.trymad.task_management.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trymad.task_management.model.User;
import com.trymad.task_management.repository.UserRepository;
import com.trymad.task_management.service.UserService;
import com.trymad.task_management.web.dto.user.UserCreateDTO;
import com.trymad.task_management.web.dto.user.UserMapper;
import com.trymad.task_management.web.dto.user.UserUpdateDTO;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Oleg");
        user.setMail("Oleg@gmail.com");
        user.setPassword("password");

        userCreateDTO = new UserCreateDTO("Oleg", "Oleg@gmail.com", "password");
        userUpdateDTO = new UserUpdateDTO("Oleg Updated", "Oleg@gmail.com", "newPassword");
    }

    @Test
    public void givenUserIdShouldReturnUser() {
        final Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.get(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Oleg", result.getName());
        assertEquals("Oleg@gmail.com", result.getMail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void givenUserIdShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        assertThrows(EntityNotFoundException.class, () -> {
            userService.get(1L);
        });
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void giverUserDtoShouldCreateUserAndReturnCreatedUser() {
        when(userMapper.toEntity(userCreateDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(userCreateDTO);

        assertNotNull(result);
        assertEquals("Oleg", result.getName());
        assertEquals("Oleg@gmail.com", result.getMail());
        assertNotNull(result.getCreated_at());
        assertNotNull(result.getUpdated_at());
        assertEquals(result.getCreated_at(), result.getUpdated_at());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void givenUserUpdateDtoShouldUpdateAndReturnUpdatedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        final Long userId = 1L;
        final LocalDateTime now = LocalDateTime.now();
        user.setCreated_at(now);
        user.setUpdated_at(now);

        User result = userService.update(userUpdateDTO, userId);

        assertNotNull(result);
        assertEquals("Oleg Updated", result.getName());
        assertEquals("Oleg@gmail.com", result.getMail());
        assertTrue(user.getUpdated_at().isAfter(now));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void givenUserUpdateDtoShouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        assertThrows(EntityNotFoundException.class, () -> {
            userService.update(userUpdateDTO, 1L);
        });
        verify(userRepository, times(1)).findById(1L);
    }
}
