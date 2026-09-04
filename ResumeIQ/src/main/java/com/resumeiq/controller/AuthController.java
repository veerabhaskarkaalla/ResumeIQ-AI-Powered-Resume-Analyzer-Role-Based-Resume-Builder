package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resumeiq.dto.AuthResponse;
import com.resumeiq.dto.LoginRequest;
import com.resumeiq.dto.RegisterRequest;
import com.resumeiq.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService
            authService;


    public AuthController(
            AuthService authService) {

        this.authService =
                authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse>
            register(
                    @RequestBody
                    RegisterRequest request) {

        return ResponseEntity.ok(
                authService.register(
                        request
                )
        );
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse>
            login(
                    @RequestBody
                    LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(
                        request
                )
        );
    }
}