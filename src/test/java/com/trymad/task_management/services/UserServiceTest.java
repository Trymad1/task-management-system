package com.trymad.task_management.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.trymad.task_management.web.dto.user.UserDTO;
import com.trymad.task_management.web.dto.user.UserMapper;
import com.trymad.task_management.web.dto.user.UserUpdateDTO;

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
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Oleg");
        user.setMail("Oleg@gmail.com");
        user.setPassword("password");

        userCreateDTO = new UserCreateDTO("Oleg", "Oleg@gmail.com", "password");
        userDTO = new UserDTO(1L, "Oleg", "Oleg@gmail.com");
        userUpdateDTO = new UserUpdateDTO("Oleg Updated", "Oleg@gmail.com", "newPassword");
    }

    @Test
    public void givenUserIdShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Oleg", result.getName());
        assertEquals("Oleg@gmail.com", result.getMail());
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
        when(userMapper.toEntity(userUpdateDTO)).thenReturn(user);
        final Long userId = 1L;
        final LocalDateTime now = LocalDateTime.now();
        user.setCreated_at(now);
        user.setUpdated_at(now);

        User result = userService.update(userUpdateDTO, userId);

        assertNotNull(result);
        assertEquals("Oleg Updated", result.getName());
        assertEquals("Oleg@gmail.com", result.getMail());
        assertTrue(user.getUpdated_at().isAfter(now));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
