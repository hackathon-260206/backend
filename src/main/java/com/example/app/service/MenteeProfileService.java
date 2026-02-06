package com.example.app.service;

import com.example.app.dto.MenteeProfileCreateResponse;
import com.example.app.entity.MenteeProfile;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.MenteeProfileRepository;
import com.example.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenteeProfileService {

    private final MenteeProfileRepository menteeProfileRepository;
    private final UserRepository userRepository;

    public MenteeProfileService(MenteeProfileRepository menteeProfileRepository, UserRepository userRepository) {
        this.menteeProfileRepository = menteeProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MenteeProfileCreateResponse upsertPortfolio(Long userId, String portfolioText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        MenteeProfile profile = menteeProfileRepository.findById(userId)
                .orElseGet(() -> new MenteeProfile(user, "", null, null, null));

        profile.setUser(user);
        profile.setPortfolioText(portfolioText);

        MenteeProfile saved = menteeProfileRepository.save(profile);
        return new MenteeProfileCreateResponse(saved.getUserId(), saved.getUserId(), "포트폴리오 입력이 완료 되었습니다.");
    }
}
