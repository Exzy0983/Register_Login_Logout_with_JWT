package com.example.loginbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private int status;
    private String timestamp;
    private String path;
}
