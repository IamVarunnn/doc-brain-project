package com.docbrain.docbrain_backend.service;

import com.docbrain.docbrain_backend.dto.RegisterUserRequest;
import com.docbrain.docbrain_backend.dto.UserDto;
import com.docbrain.docbrain_backend.entity.Role;
import com.docbrain.docbrain_backend.entity.User;
import com.docbrain.docbrain_backend.respositry.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUser(@Valid RegisterUserRequest registerUserRequest) {

        if(registerUserRequest.username().isBlank()){
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if(userRepository.existsByUsername(registerUserRequest.username())){
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerUserRequest.username());
        user.setEmail(registerUserRequest.email());
        user.setPassword(passwordEncoder.encode(registerUserRequest.password()));
        user.setRole(Role.USER);

        User savedUser =  userRepository.save(user);

        return new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }
}
