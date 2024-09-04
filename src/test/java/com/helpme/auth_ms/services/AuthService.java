package com.helpme.auth_ms.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.helpme.auth_ms.constants.Constants;
import com.helpme.auth_ms.exceptions.AuthenticationFailedException;
import com.helpme.auth_ms.model.Login;
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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Constants constants;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Login login;
    private UUID userId;
    private Algorithm algorithm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Roles.USER);

        login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("validPassword");

        algorithm = Algorithm.HMAC256("secret");

        when(constants.getAlgorithm()).thenReturn(algorithm);
        when(constants.getJwtExpirationTime()).thenReturn(new Date(System.currentTimeMillis() + 1000 * 60 * 60).toInstant()); // 1 hour expiration
    }

    @Test
    void authenticate_shouldReturnJwtToken() {
        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(login.getPassword(), testUser.getPassword())).thenReturn(true);

        String token = authService.authenticate(login);

        assertNotNull(token);
        verify(userRepository).findByEmail(login.getEmail());
        verify(passwordEncoder).matches(login.getPassword(), testUser.getPassword());

        String expectedToken = JWT.create()
                .withIssuer("helpme-auth")
                .withSubject(testUser.getId().toString())
                .withClaim("role", testUser.getRole().toString())
                .withExpiresAt(constants.getJwtExpirationTime())
                .sign(algorithm);

        assertEquals(expectedToken.split("\\.")[0], token.split("\\.")[0]); // Validate only the header part, as signature is time-dependent
    }

    @Test
    void authenticate_shouldThrowAuthenticationFailedException_whenUserNotFound() {
        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> authService.authenticate(login));

        verify(userRepository).findByEmail(login.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void authenticate_shouldThrowAuthenticationFailedException_whenPasswordDoesNotMatch() {
        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(login.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(AuthenticationFailedException.class, () -> authService.authenticate(login));

        verify(userRepository).findByEmail(login.getEmail());
        verify(passwordEncoder).matches(login.getPassword(), testUser.getPassword());
    }
}
