package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionStatusResponse {

    @JsonProperty("logged_in")
    private boolean loggedIn;

    @JsonProperty("user_id")
    private Long userId;

    public SessionStatusResponse(boolean loggedIn, Long userId) {
        this.loggedIn = loggedIn;
        this.userId = userId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Long getUserId() {
        return userId;
    }
}
