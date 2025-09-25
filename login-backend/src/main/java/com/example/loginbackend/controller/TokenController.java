package com.example.loginbackend.controller;

import com.example.loginbackend.dto.RefreshTokenRequestDTO;
import com.example.loginbackend.dto.RefreshTokenResponseDTO;
import com.example.loginbackend.entity.User;
import com.example.loginbackend.security.JwtUtil;
import com.example.loginbackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final RefreshTokenService refreshTokenService;
    private JwtUtil jwtUtil;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {

        try {
            User user = refreshTokenService.getUserByRefreshToken(request.getRefreshToken());

            String newAccessToken = jwtUtil.generateToken(user.getLoginId(), user.getRole().toString());

            RefreshTokenResponseDTO response = RefreshTokenResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .expirationTime(System.currentTimeMillis() + 3600000L)
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequestDTO request) {
        try {
            refreshTokenService.deleteRefreshToken(request.getRefreshToken());
            return ResponseEntity.ok("로그아웃이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

