package com.example.app.dto;

import jakarta.validation.constraints.NotNull;

public class UserLogoutRequest {

    @NotNull
    private Long id;

    public UserLogoutRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
