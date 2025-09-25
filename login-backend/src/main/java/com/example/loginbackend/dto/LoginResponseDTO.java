package com.example.loginbackend.dto;

import com.example.loginbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    private String loginId;
    private String name;
    private String accessToken;
    private String refreshToken;
    private long expirationTime;
    private Role role;
}
