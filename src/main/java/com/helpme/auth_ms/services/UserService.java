package com.helpme.auth_ms.services;

import com.helpme.auth_ms.exceptions.UserAlreadyExistsException;
import com.helpme.auth_ms.model.User;
import com.helpme.auth_ms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        Optional<User> userAlreadyExists = this.userRepository.findByEmail(user.getEmail());
        if (userAlreadyExists.isPresent()) {
            throw new UserAlreadyExistsException();
        }
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
        User savedUser = this.userRepository.save(user);
        return savedUser;
    }


}
