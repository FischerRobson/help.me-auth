package com.helpme.auth_ms.services;

import com.helpme.auth_ms.exceptions.InvalidPasswordException;
import com.helpme.auth_ms.exceptions.UserAlreadyExistsException;
import com.helpme.auth_ms.exceptions.UserNotFoundException;
import com.helpme.auth_ms.model.Roles;
import com.helpme.auth_ms.model.User;
import com.helpme.auth_ms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        User validatedUser = this.validateNewUserCreation(user);

        validatedUser.setRole(Roles.USER);
        return this.userRepository.save(validatedUser);

    }

    public User createSupport(User user) {
        User validatedUser = this.validateNewUserCreation(user);

        validatedUser.setRole(Roles.SUPPORT);
        return this.userRepository.save(validatedUser);
    }

    public User createAdmin(User user) {
        User validatedUser = this.validateNewUserCreation(user);

        validatedUser.setRole(Roles.ADMIN);
        return this.userRepository.save(validatedUser);
    }

    public void changeRole(UUID userId, String newRole) {

        Roles updatedRole = Roles.fromString(newRole);

        User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setRole(updatedRole);
        this.userRepository.save(user);
    }

    private User validateNewUserCreation(User user) {
        if (user.getPassword().length() < 8 || user.getPassword().length() > 16) {
            throw new InvalidPasswordException();
        }

        Optional<User> userAlreadyExists = this.userRepository.findByEmail(user.getEmail());
        if (userAlreadyExists.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return user;
    }
}
