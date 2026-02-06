package com.example.app.dto;

import com.example.app.entity.Role;

public class UserLoginResponse {

    private Long id;
    private String email;
    private String name;
    private Role role;

    public UserLoginResponse(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
