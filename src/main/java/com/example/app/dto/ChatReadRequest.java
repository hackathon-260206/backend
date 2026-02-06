package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ChatReadRequest {

    @JsonProperty("partner_id")
    @NotNull
    private Long partnerId;

    public ChatReadRequest() {
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
}