package com.example.loginbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<?> getUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "사용자 프로필 조회 성공");
        response.put("role", "USER");
        response.put("accessibleBy", "일반 사용자");
        return ResponseEntity.ok(response);
    }
}
