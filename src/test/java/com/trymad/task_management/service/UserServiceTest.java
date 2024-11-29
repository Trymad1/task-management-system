package com.trymad.task_management.service;

import com.trymad.task_management.exception.UserAlreadyExistsException;
import com.trymad.task_management.model.Role;
import com.trymad.task_management.model.RoleEntity;
import com.trymad.task_management.model.User;
import com.trymad.task_management.repository.UserRepository;
import com.trymad.task_management.web.dto.user.UserCreateDTO;
import com.trymad.task_management.web.dto.user.UserUpdateDTO;
import com.trymad.task_management.web.dto.user.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private UserDetails userDetails;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setMail("test@mail.com");
        user.setName("Test User");
        user.setPassword("encodedpassword");
        RoleEntity userRole = new RoleEntity();
        userRole.setName(Role.USER);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userCreateDTO = new UserCreateDTO("Test User", "test@mail.com", "password");
        userUpdateDTO = new UserUpdateDTO("Updated User", "test@mail.com", "newpassword");
        roleEntity = new RoleEntity();
        roleEntity.setName(Role.USER);
    }

    @Test
    void testCreateUser_Success() {
        user.setRoles(new HashSet<>());
        when(userRepository.existsByMail(userCreateDTO.mail())).thenReturn(false);
        when(userMapper.toEntity(userCreateDTO)).thenReturn(user);
        when(passwordEncoder.encode(userCreateDTO.password())).thenReturn("encodedpassword");
        when(roleService.get(Role.USER)).thenReturn(roleEntity);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(userCreateDTO, Role.USER);

        assertNotNull(createdUser);
        assertEquals("test@mail.com", createdUser.getMail());
        assertEquals("Test User", createdUser.getName());
        assertEquals(1, createdUser.getRoles().size());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        when(userRepository.existsByMail(userCreateDTO.mail())).thenReturn(true);
        when(userMapper.toEntity(userCreateDTO)).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(userCreateDTO, Role.USER));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.get(1L);

        assertNotNull(foundUser);
        assertEquals(user.getMail(), foundUser.getMail());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.get(1L));
    }

    @Test
    void testGetUserByMail_Success() {
        when(userRepository.findByMail("test@mail.com")).thenReturn(Optional.of(user));

        User foundUser = userService.get("test@mail.com");

        assertNotNull(foundUser);
        assertEquals(user.getMail(), foundUser.getMail());
    }

    @Test
    void testGetUserByMail_NotFound() {
        when(userRepository.findByMail("nonexistent@mail.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.get("nonexistent@mail.com"));
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user.getMail());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User updatedUser = userService.update(userUpdateDTO, 1L);

        assertNotNull(updatedUser);
        assertEquals("newpassword", updatedUser.getPassword());
        assertEquals("Updated User", updatedUser.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_AccessDenied() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("dummy@gmail.com", null, null));

        assertThrows(AccessDeniedException.class, () -> userService.update(userUpdateDTO, 1L));
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByMail("test@mail.com")).thenReturn(Optional.of(user));

        UserDetails loadedUser = userService.loadUserByUsername("test@mail.com");

        assertNotNull(loadedUser);
        assertEquals("test@mail.com", loadedUser.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByMail("nonexistent@mail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent@mail.com"));
    }

    @Test
    void testGrantAdminRole() {
        RoleEntity roleEntityAdmin = new RoleEntity();
        roleEntityAdmin.setName(Role.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleService.get(Role.ADMIN)).thenReturn(roleEntityAdmin);

        User updatedUser = userService.grantAdminRole(1L);

        assertTrue(updatedUser.getRoles().stream().anyMatch(role -> role.getName().equals(Role.ADMIN)));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetExistsReference_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.getReferenceById(1L)).thenReturn(user);

        User userReference = userService.getExistsReference(1L);

        assertNotNull(userReference);
        assertEquals(user.getMail(), userReference.getMail());
    }

    @Test
    void testGetExistsReference_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.getExistsReference(1L));
    }
}