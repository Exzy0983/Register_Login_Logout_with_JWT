package com.example.loginbackend.controller;

import com.example.loginbackend.entity.User;
import com.example.loginbackend.repository.UserRepository;
import com.example.loginbackend.security.JwtUtil;
import com.example.loginbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal String loginId) {
        User user = userService.getUserProfile(loginId);
        return ResponseEntity.ok(user);
    }
}
