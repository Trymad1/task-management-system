package com.trymad.task_management.web;

import com.trymad.task_management.TestSecurityConfig;
import com.trymad.task_management.model.User;
import com.trymad.task_management.service.UserService;
import com.trymad.task_management.web.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.trymad.task_management.service.AuthenticationService;
import com.trymad.task_management.service.JwtTokenProvider;
import com.trymad.task_management.util.ErrorResponseSupplyer;
import com.trymad.task_management.util.JwtErrorResponseWriter;
import com.trymad.task_management.util.PublicPathStorage;
import com.trymad.task_management.web.dto.user.UserMapperImpl;

@WebMvcTest(UserController.class)
@Import({ ErrorResponseSupplyer.class, UserMapperImpl.class, AuthenticationService.class, JwtErrorResponseWriter.class,
        JwtTokenProvider.class, PublicPathStorage.class, TestSecurityConfig.class })
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @MockitoBean
    AuthenticationManager authManager;

    private User user;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setName("Oleg");
        user.setMail("Oleg@gmail.com");
        user.setPassword("password");
        user.setId(1L);
    }

    @Test
    @WithMockUser(username = "Oleg@gmail.com", roles = "USER")
    public void getAllUsers_Success() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Oleg")))
                .andExpect(jsonPath("$[0].mail", is("Oleg@gmail.com")));
    }

    @Test
    @WithMockUser(username = "Oleg@gmail.com", roles = "USER")
    public void getUserById_Success() throws Exception {
        when(userService.get(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Oleg")))
                .andExpect(jsonPath("$.mail", is("Oleg@gmail.com")));
    }


}
