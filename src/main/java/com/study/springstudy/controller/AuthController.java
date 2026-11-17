package com.study.springstudy.controller;

import com.study.springstudy.dto.LoginRequest;
import com.study.springstudy.service.AuthService;
import org.springframework.web.bind.annotation.*;
import com.study.springstudy.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.study.springstudy.dto.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String token = authService.login(request);

        LoginResponse response = new LoginResponse(token);

        return ResponseEntity.ok(response);
    }
}