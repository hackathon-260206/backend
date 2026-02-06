package com.example.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mentee_profiles")
public class MenteeProfile {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String techStack;

    @Column(length = 100)
    private String university;

    @Column(columnDefinition = "TEXT")
    private String portfolioDescription;

    @Column(length = 512)
    private String githubUrl;

    protected MenteeProfile() {
    }

    public MenteeProfile(User user, String techStack, String university, String portfolioDescription, String githubUrl) {
        this.user = user;
        this.techStack = techStack;
        this.university = university;
        this.portfolioDescription = portfolioDescription;
        this.githubUrl = githubUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public String getTechStack() {
        return techStack;
    }

    public String getUniversity() {
        return university;
    }

    public String getPortfolioDescription() {
        return portfolioDescription;
    }

    public String getGithubUrl() {
        return githubUrl;
    }
}
