package com.example.app.repository;

import com.example.app.entity.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {
}
