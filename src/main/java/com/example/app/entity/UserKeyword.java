package com.example.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_keywords")
public class UserKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    protected UserKeyword() {
    }

    public UserKeyword(User user, Keyword keyword) {
        this.user = user;
        this.keyword = keyword;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Keyword getKeyword() {
        return keyword;
    }
}
