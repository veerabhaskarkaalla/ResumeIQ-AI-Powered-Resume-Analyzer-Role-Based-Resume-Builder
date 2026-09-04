package com.resumeiq.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resumeiq.dto.AuthResponse;
import com.resumeiq.dto.LoginRequest;
import com.resumeiq.dto.RegisterRequest;
import com.resumeiq.entity.User;
import com.resumeiq.repository.UserRepository;
import com.resumeiq.security.JwtService;

@Service
public class AuthService {

    private final UserRepository
            userRepository;

    private final PasswordEncoder
            passwordEncoder;

    private final JwtService
            jwtService;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.jwtService =
                jwtService;
    }


    public AuthResponse register(
            RegisterRequest request) {

        validateRegister(request);


        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();


        if (userRepository
                .existsByEmail(email)) {

            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }


        User user =
                new User();


        user.setName(
                request.getName()
                        .trim()
        );


        user.setEmail(email);


        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        user.setCreatedAt(
                LocalDateTime.now()
        );


        User saved =
                userRepository.save(
                        user
                );


        String token =
                jwtService.generateToken(
                        saved
                );


        return new AuthResponse(
                token,
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }


    public AuthResponse login(
            LoginRequest request) {

        if (request == null
                || request.getEmail() == null
                || request.getEmail().isBlank()
                || request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Email and password are required"
            );
        }


        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();


        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Invalid email or password"
                                        )
                        );


        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPasswordHash()
                );


        if (!passwordMatches) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }


        String token =
                jwtService.generateToken(
                        user
                );


        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }


    private void validateRegister(
            RegisterRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Registration data is required"
            );
        }


        if (request.getName() == null
                || request.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Name is required"
            );
        }


        if (request.getEmail() == null
                || request.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email is required"
            );
        }


        if (!request.getEmail()
                .contains("@")) {

            throw new IllegalArgumentException(
                    "Enter a valid email address"
            );
        }


        if (request.getPassword() == null
                || request.getPassword().length() < 8) {

            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters"
            );
        }
    }
}