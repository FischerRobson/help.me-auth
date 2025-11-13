package com.example.challenge.services;

import com.example.challenge.exceptions.UserAlreadyExistsException;
import com.example.challenge.exceptions.UserNotFoundException;
import com.example.challenge.model.ExternalProject;
import com.example.challenge.model.dto.UserResponseDTO;
import com.example.challenge.model.Roles;
import com.example.challenge.model.User;
import com.example.challenge.model.dto.UserUpdateDTO;
import com.example.challenge.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        User validatedUser = this.validateNewUserCreation(user);

        validatedUser.setRole(Roles.USER);
        return this.userRepository.save(validatedUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public User createAdmin(User user) {
        User validatedUser = this.validateNewUserCreation(user);

        validatedUser.setRole(Roles.ADMIN);
        return this.userRepository.save(validatedUser);
    }

    public void changeRole(UUID userId, Roles newRole) {
        User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setRole(newRole);
        log.debug("Changing user {} to {}", userId, newRole);
        this.userRepository.save(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        log.debug("Searching all users");
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::from)
                .toList();
    }

    public User getUserById(UUID id) {
        log.debug("Searching user {}", id);
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public User updateUser(UUID id, UserUpdateDTO dto) {
        User existing = getUserById(id);

        log.debug("Updating user: {}", existing.getId());

        if (dto.getName() != null && !dto.getName().isBlank()) {
            log.debug("Updating name");
            existing.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())) {
            userRepository.findByEmail(dto.getEmail()).ifPresent(e -> {
                throw new UserAlreadyExistsException();
            });
            log.debug("Updating email");
            existing.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            log.debug("Updating password");
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(existing);
    }

    private User validateNewUserCreation(User user) {

        Optional<User> userAlreadyExists = this.userRepository.findByEmail(user.getEmail());
        if (userAlreadyExists.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return user;
    }
}
