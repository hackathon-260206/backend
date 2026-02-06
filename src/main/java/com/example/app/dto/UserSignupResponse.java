package com.example.app.dto;

public class UserSignupResponse {

    private Long id;
    private String message;

    public UserSignupResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
