package com.helpme.auth_ms.services;

import com.helpme.auth_ms.exceptions.InvalidPasswordException;
import com.helpme.auth_ms.exceptions.UserAlreadyExistsException;
import com.helpme.auth_ms.exceptions.UserNotFoundException;
import com.helpme.auth_ms.model.Roles;
import com.helpme.auth_ms.model.User;
import com.helpme.auth_ms.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("validPass123");

        userId = UUID.randomUUID();
    }

    @Test
    void createUser_shouldSaveUserWithRoleUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        User result = userService.createUser(testUser);

        assertEquals(Roles.USER, result.getRole());
        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode("validPass123");
    }

    @Test
    void createSupport_shouldSaveUserWithRoleSupport() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        User result = userService.createSupport(testUser);

        assertEquals(Roles.SUPPORT, result.getRole());
        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode("validPass123");
    }

    @Test
    void createAdmin_shouldSaveUserWithRoleAdmin() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        User result = userService.createAdmin(testUser);

        assertEquals(Roles.ADMIN, result.getRole());
        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode("validPass123");
    }

    @Test
    void changeRole_shouldUpdateUserRole() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        userService.changeRole(userId, Roles.ADMIN);

        assertEquals(Roles.ADMIN, testUser.getRole());
        verify(userRepository).save(testUser);
    }

    @Test
    void changeRole_userNotFound_shouldThrowUserNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changeRole(userId, Roles.ADMIN));
    }

    @Test
    void validateNewUserCreation_passwordTooShort_shouldThrowInvalidPasswordException() {
        testUser.setPassword("short");

        assertThrows(InvalidPasswordException.class, () -> userService.createUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void validateNewUserCreation_userAlreadyExists_shouldThrowUserAlreadyExistsException() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }
}
