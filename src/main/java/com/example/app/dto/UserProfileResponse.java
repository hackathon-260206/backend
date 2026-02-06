package com.example.app.dto;

import com.example.app.entity.Gender;
import com.example.app.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class UserProfileResponse {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String name;
    private String email;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    private Gender gender;
    private Role role;

    public UserProfileResponse(Long id, Long userId, String name, String email, LocalDate birthDate, Gender gender, Role role) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public Role getRole() {
        return role;
    }
}
