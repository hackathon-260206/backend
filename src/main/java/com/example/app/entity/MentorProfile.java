package com.example.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mentor_profiles")
public class MentorProfile {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String techStack;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 100, nullable = false)
    private String company;

    @Column(length = 512)
    private String githubUrl;

    @Column(nullable = false)
    private Integer mentoringCount = 0;

    protected MentorProfile() {
    }

    public MentorProfile(User user, String techStack, Integer price, String company, String githubUrl) {
        this.user = user;
        this.techStack = techStack;
        this.price = price;
        this.company = company;
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
