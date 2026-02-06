package com.example.app.dto;

import com.example.app.entity.Gender;
import com.example.app.entity.Role;
import java.time.LocalDate;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private Gender gender;
    private Role role;

    public UserResponse(Long id, String name, String email, LocalDate birthDate, Gender gender, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
    }

    public Long getId() {
        return id;
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
