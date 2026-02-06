package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MenteeProfileCreateResponse {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String message;
    private List<String> keywords;

    public MenteeProfileCreateResponse(Long id, Long userId, String message, List<String> keywords) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.keywords = keywords;
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

    public List<String> getKeywords() {
        return keywords;
    }
}
