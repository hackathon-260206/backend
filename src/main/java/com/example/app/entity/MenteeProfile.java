package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mentee_profiles")
public class MenteeProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(nullable = false)
    private String techStack;

    @Column(length = 100)
    private String university;

    @Column(length = 512)
    private String portfolioUrl;

    @Lob
    @Column(name = "portfolio_text")
    private String portfolioText;

    @Column(length = 512)
    private String githubUrl;

    protected MenteeProfile() {
    }

    public MenteeProfile(User user, String techStack, String university, String portfolioUrl, String githubUrl) {
        this.user = user;
        this.techStack = techStack;
        this.university = university;
        this.portfolioUrl = portfolioUrl;
        this.portfolioText = null;
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

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public String getPortfolioText() {
        return portfolioText;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public void setPortfolioText(String portfolioText) {
        this.portfolioText = portfolioText;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }
}
