package com.example.app.service;

import com.example.app.dto.MenteeProfileCreateRequest;
import com.example.app.dto.MenteeProfileCreateResponse;
import com.example.app.entity.MenteeProfile;
import com.example.app.entity.User;
import com.example.app.repository.MenteeProfileRepository;
import com.example.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenteeService {

    private final MenteeProfileRepository menteeProfileRepository;
    private final UserRepository userRepository;

    public MenteeService(MenteeProfileRepository menteeProfileRepository, UserRepository userRepository) {
        this.menteeProfileRepository = menteeProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MenteeProfileCreateResponse createProfile(Long userId, MenteeProfileCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        MenteeProfile profile = new MenteeProfile(
                user,
                request.getTechStack(),
                request.getUniversity(),
                null, // portfolioUrl - not in request spec
                request.getGithubUrl()
        );

        MenteeProfile savedProfile = menteeProfileRepository.save(profile);

        return new MenteeProfileCreateResponse(
                savedProfile.getUserId(),
                savedProfile.getUser().getId(),
                "멘티 프로필 생성이 되었습니다."
        );
    }
}
