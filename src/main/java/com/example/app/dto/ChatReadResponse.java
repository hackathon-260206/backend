package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatReadResponse {

    @JsonProperty("partner_id")
    private Long partnerId;

    @JsonProperty("read_by")
    private Long readBy;

    private String message;

    public ChatReadResponse(Long partnerId, Long readBy, String message) {
        this.partnerId = partnerId;
        this.readBy = readBy;
        this.message = message;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public Long getReadBy() {
        return readBy;
    }

    public String getMessage() {
        return message;
    }
}