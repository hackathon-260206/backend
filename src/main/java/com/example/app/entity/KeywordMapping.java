package com.example.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "keyword_mapping")
public class KeywordMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    protected KeywordMapping() {
    }

    public KeywordMapping(User user, Keyword keyword) {
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
}
