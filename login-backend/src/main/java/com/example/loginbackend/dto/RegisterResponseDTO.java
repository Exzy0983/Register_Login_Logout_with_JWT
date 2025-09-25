package com.example.loginbackend.dto;

import com.example.loginbackend.entity.Gender;
import com.example.loginbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterResponseDTO {
    private String loginId;
    private String name;
    private LocalDateTime regDate;
    private Role role;
    private Gender gender;
}
