package com.example.app.service;

import com.example.app.dto.MentorProfileCreateRequest;
import com.example.app.dto.MentorProfileCreateResponse;
import com.example.app.entity.MentorProfile;
import com.example.app.entity.User;
import com.example.app.repository.MentorProfileRepository;
import com.example.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MentorService {

    private final MentorProfileRepository mentorProfileRepository;
    private final UserRepository userRepository;

    public MentorService(MentorProfileRepository mentorProfileRepository, UserRepository userRepository) {
        this.mentorProfileRepository = mentorProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MentorProfileCreateResponse createProfile(Long userId, MentorProfileCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        
        MentorProfile profile = new MentorProfile(
                user,
                request.getTechStack(),
                request.getPrice(),
                request.getCompany(),
                request.getGithubUrl(),
                0 // Initial mentoring count
        );

        MentorProfile savedProfile = mentorProfileRepository.save(profile);

        return new MentorProfileCreateResponse(
                savedProfile.getUserId(),
                savedProfile.getUser().getId(),
                "멘토 프로필 생성이 되었습니다."
        );
    }
}
