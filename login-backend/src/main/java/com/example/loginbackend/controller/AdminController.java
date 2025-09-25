package com.example.loginbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "전체 사용자 목록 조회 성공");
        response.put("role", "ADMIN");
        response.put("userCount", 100);
        response.put("accessibleBy", "관리자만");
        return ResponseEntity.ok(response);
    }
}
