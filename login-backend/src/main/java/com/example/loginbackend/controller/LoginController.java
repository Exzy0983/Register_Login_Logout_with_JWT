package com.example.loginbackend.controller;

import com.example.loginbackend.dto.LoginRequestDTO;
import com.example.loginbackend.dto.LoginResponseDTO;
import com.example.loginbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO loginResult = userService.authenticate(dto);
        return ResponseEntity.status(200).body(loginResult);
    }
}
