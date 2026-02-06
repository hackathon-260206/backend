package com.example.app.service;

import com.example.app.dto.MenteeProfileCreateResponse;
import com.example.app.entity.Keyword;
import com.example.app.entity.KeywordMapping;
import com.example.app.entity.MenteeProfile;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.KeywordMappingRepository;
import com.example.app.repository.KeywordRepository;
import com.example.app.repository.MenteeProfileRepository;
import com.example.app.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenteeProfileService {

    private final MenteeProfileRepository menteeProfileRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final KeywordMappingRepository keywordMappingRepository;
    private final AiKeywordClient aiKeywordClient;

    public MenteeProfileService(MenteeProfileRepository menteeProfileRepository,
                                UserRepository userRepository,
                                KeywordRepository keywordRepository,
                                KeywordMappingRepository keywordMappingRepository,
                                AiKeywordClient aiKeywordClient) {
        this.menteeProfileRepository = menteeProfileRepository;
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
        this.keywordMappingRepository = keywordMappingRepository;
        this.aiKeywordClient = aiKeywordClient;
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

        List<String> keywords = aiKeywordClient.extractKeywords(portfolioText);
        int added = 0;
        for (String keywordText : keywords) {
            if (keywordText == null) {
                continue;
            }
            String trimmed = keywordText.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            Keyword keyword = keywordRepository.findByName(trimmed)
                    .orElseGet(() -> keywordRepository.save(new Keyword(trimmed)));

            boolean exists = keywordMappingRepository.existsByUser_IdAndKeyword_Id(userId, keyword.getId());
            if (!exists) {
                keywordMappingRepository.save(new KeywordMapping(user, keyword));
            }
            added++;
            if (added >= 5) {
                break;
            }
        }

        return new MenteeProfileCreateResponse(saved.getUserId(), saved.getUserId(), "Portfolio submitted.");
    }
}

