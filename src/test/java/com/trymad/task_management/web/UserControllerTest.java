package com.trymad.task_management.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.User;
import com.trymad.task_management.service.UserService;
import com.trymad.task_management.web.controller.UserController;
import com.trymad.task_management.web.dto.user.UserCreateDTO;
import com.trymad.task_management.web.dto.user.UserDTO;
import com.trymad.task_management.web.dto.user.UserMapper;
import com.trymad.task_management.web.dto.user.UserUpdateDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;
    private User user;

    @BeforeEach
    void setUp() {

        userDTO = new UserDTO(1L, "Oleg", "Oleg@gmail.com");
        userCreateDTO = new UserCreateDTO("Oleg", "Oleg@gmail.com", "password123");
        userUpdateDTO = new UserUpdateDTO("Oleg Updated", "OlegUpdated@gmail.com", "newpassword123");

        user = new User();
        user.setId(1L);
        user.setName("Oleg");
        user.setMail("Oleg@gmail.com");
        user.setPassword("password123");
    }

    @Test
    void givenValidIdWhenGetUserThenReturnUserDTO() throws Exception {

        when(userService.get(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Oleg"))
                .andExpect(jsonPath("$.mail").value("Oleg@gmail.com"));
    }

    @Test
    void givenValidUserCreateDTOWhenCreateUserThenReturnUserDTO() throws Exception {
        when(userService.create(userCreateDTO, Role.USER)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Oleg"))
                .andExpect(jsonPath("$.mail").value("Oleg@gmail.com"));
    }

    @Test
    void givenValidUserUpdateDTOWhenUpdateUserThenReturnUpdatedUserDTO() throws Exception {

        when(userService.update(userUpdateDTO, 1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Oleg"))
                .andExpect(jsonPath("$.mail").value("Oleg@gmail.com"));
    }
}