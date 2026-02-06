package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MentorRecommendationItem {

    private Long id;

    private String name;

    private Integer price;
    private String company;

    @JsonProperty("github_url")
    private String githubUrl;

    public MentorRecommendationItem(Long mentorId,
                                    String mentorName,
                                    Integer price,
                                    String company,
                                    String githubUrl) {
        this.id = mentorId;
        this.name = mentorName;
        this.price = price;
        this.company = company;
        this.githubUrl = githubUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
}
