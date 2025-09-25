package com.example.loginbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RefreshTokenResponseDTO {
    private String accessToken;
    private Long expirationTime;
}
