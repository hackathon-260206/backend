package com.example.app.dto;

import com.example.app.entity.Gender;
import com.example.app.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class MenteeProfileResponse extends UserProfileResponse {

    @JsonProperty("tech_stack")
    private String techStack;

    private String university;

    @JsonProperty("portfolio_text")
    private String portfolioText;

    @JsonProperty("github_url")
    private String githubUrl;

    public MenteeProfileResponse(Long id, Long userId, String name, String email, LocalDate birthDate, Gender gender, Role role,
                                 String techStack, String university, String portfolioText, String githubUrl) {
        super(id, userId, name, email, birthDate, gender, role);
        this.techStack = techStack;
        this.university = university;
        this.portfolioText = portfolioText;
        this.githubUrl = githubUrl;
    }

    public String getTechStack() {
        return techStack;
    }

    public String getUniversity() {
        return university;
    }

    public String getPortfolioText() {
        return portfolioText;
    }

    public String getGithubUrl() {
        return githubUrl;
    }
}
