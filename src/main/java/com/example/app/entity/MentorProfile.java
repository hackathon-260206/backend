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
@Table(name = "mentor_profiles")
public class MentorProfile {

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

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(length = 512)
    private String githubUrl;

    @Column(nullable = false)
    private Integer mentoringCount;

    protected MentorProfile() {
    }

    public MentorProfile(User user, String techStack, Integer price, String company, String githubUrl, Integer mentoringCount) {
        this.user = user;
        this.techStack = techStack;
        this.price = price;
        this.company = company;
        this.githubUrl = githubUrl;
        this.mentoringCount = mentoringCount;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public void setMentoringCount(Integer mentoringCount) {
        this.mentoringCount = mentoringCount;
    }
}
