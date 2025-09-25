package com.example.loginbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDTO {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String gender;
}
