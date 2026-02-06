package com.example.app.dto;

import com.example.app.entity.Gender;
import com.example.app.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class MentorProfileResponse extends UserProfileResponse {

    @JsonProperty("tech_stack")
    private String techStack;

    private Integer price;
    private String company;

    @JsonProperty("github_url")
    private String githubUrl;

    @JsonProperty("mentoring_count")
    private Integer mentoringCount;

    public MentorProfileResponse(Long id, Long userId, String name, String email, LocalDate birthDate, Gender gender, Role role,
                                 String techStack, Integer price, String company, String githubUrl, Integer mentoringCount) {
        super(id, userId, name, email, birthDate, gender, role);
        this.techStack = techStack;
        this.price = price;
        this.company = company;
        this.githubUrl = githubUrl;
        this.mentoringCount = mentoringCount;
    }

    public String getTechStack() {
        return techStack;
    }

    public Integer getPrice() {
        return price;
    }

    public String getCompany() {
        return company;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public Integer getMentoringCount() {
        return mentoringCount;
    }
}
