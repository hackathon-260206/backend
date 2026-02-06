package com.example.app.dto;

public class UserLogoutResponse {

    private String message;

    public UserLogoutResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
