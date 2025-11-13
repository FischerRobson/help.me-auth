package com.example.challenge.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.challenge.constants.Constants;
import com.example.challenge.exceptions.AuthenticationFailedException;
import com.example.challenge.model.Login;
import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import com.example.challenge.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Constants constants;

    private AuthService authService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        constants = mock(Constants.class);

        authService = new AuthService(
                userRepository,
                passwordEncoder,
                constants
        );
    }

    @Test
    void authenticate_ShouldReturnJwt_WhenCredentialsAreValid() {
        // Arrange
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setEmail("test@mail.com");
        user.setPassword("encoded");
        user.setRole(Roles.ADMIN);

        Login login = new Login("test@mail.com", "123456");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encoded"))
                .thenReturn(true);

        // Use REAL algorithm for signing
        Algorithm algorithm = Algorithm.HMAC256("test-secret");
        when(constants.getAlgorithm()).thenReturn(algorithm);

        // Provide a real expiration date
        when(constants.getJwtExpirationTime())
                .thenReturn(Date.from(Instant.now().plusSeconds(3600)).toInstant());

        // Act
        String token = authService.authenticate(login);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        DecodedJWT decoded = JWT.decode(token);

        assertEquals("helpme-auth", decoded.getIssuer());
        assertEquals(id.toString(), decoded.getSubject());
        assertEquals("ADMIN", decoded.getClaim("role").asString());
        assertEquals("test@mail.com", decoded.getClaim("email").asString());
        assertNotNull(decoded.getId());

        verify(userRepository).findByEmail("test@mail.com");
        verify(passwordEncoder).matches("123456", "encoded");
    }


    @Test
    void authenticate_ShouldThrow_WhenEmailNotFound() {
        Login login = new Login("notfound@mail.com", "123");

        when(userRepository.findByEmail("notfound@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> authService.authenticate(login));
    }

    @Test
    void authenticate_ShouldThrow_WhenPasswordDoesNotMatch() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        Login login = new Login("test@mail.com", "wrong");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encoded"))
                .thenReturn(false);

        assertThrows(AuthenticationFailedException.class, () -> authService.authenticate(login));
    }
}
