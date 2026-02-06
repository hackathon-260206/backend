package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MentorProfileCreateResponse {

    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    private String message;

    public MentorProfileCreateResponse(Long id, Long userId, String message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
