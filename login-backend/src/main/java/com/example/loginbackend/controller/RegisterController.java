package com.example.loginbackend.controller;

import com.example.loginbackend.dto.RegisterRequestDTO;
import com.example.loginbackend.dto.RegisterResponseDTO;
import com.example.loginbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RegisterController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {
        RegisterResponseDTO registerResult = userService.register(dto);
        return ResponseEntity.status(201).body(registerResult); // 201 Created

    }
}
