package com.example.app.repository;

import com.example.app.entity.MenteeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeProfileRepository extends JpaRepository<MenteeProfile, Long> {
}
