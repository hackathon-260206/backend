package com.example.app.repository;

import com.example.app.entity.MentorProfile;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {
    List<MentorProfile> findByUserIdIn(Collection<Long> userIds);
}
