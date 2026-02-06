package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class MenteeProfileCreateRequest {

    @NotBlank
    @JsonProperty("portfolio_text")
    private String portfolioText;

    public MenteeProfileCreateRequest() {
    }

    public String getPortfolioText() {
        return portfolioText;
    }

    public void setPortfolioText(String portfolioText) {
        this.portfolioText = portfolioText;
    }
}
