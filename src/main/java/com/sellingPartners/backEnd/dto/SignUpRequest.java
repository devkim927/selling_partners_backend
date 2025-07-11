package com.sellingPartners.backEnd.dto;

import com.sellingPartners.backEnd.entity.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private Role role;
    private String phoneNumber;
    private String email;
    private String name;
}
