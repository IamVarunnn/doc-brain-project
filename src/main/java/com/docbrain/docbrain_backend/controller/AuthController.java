package com.docbrain.docbrain_backend.controller;

import com.docbrain.docbrain_backend.dto.LoginRequest;
import com.docbrain.docbrain_backend.dto.LoginResponse;
import com.docbrain.docbrain_backend.dto.RegisterUserRequest;
import com.docbrain.docbrain_backend.dto.UserDto;
import com.docbrain.docbrain_backend.entity.User;
import com.docbrain.docbrain_backend.respositry.UserRepository;
import com.docbrain.docbrain_backend.service.CustomUserDetail;
import com.docbrain.docbrain_backend.service.JWTService;
import com.docbrain.docbrain_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        // To do
        // authenticate and return the token and user
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        );
        Authentication authenticated = authenticationManager.authenticate(authentication);

        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new RuntimeException("User not found"));

        String token = jwtService.generateToken(new CustomUserDetail(user));

        LoginResponse response = new LoginResponse(token, new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserRequest registerUserRequest){

        UserDto userDto = userService.registerUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
