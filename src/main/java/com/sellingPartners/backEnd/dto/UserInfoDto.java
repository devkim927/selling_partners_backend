package com.sellingPartners.backEnd.dto;

import com.sellingPartners.backEnd.entity.Role;

public class UserInfoDto {
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;

    // 생성자
    public UserInfoDto(String name, String email, String phoneNumber, Role role) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Role getRole() { return role; }
}
