package com.example.app.service;

import com.example.app.dto.MentorRecommendationItem;
import com.example.app.dto.MentorRecommendationResponse;
import com.example.app.entity.KeywordMapping;
import com.example.app.entity.MentorProfile;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.exception.ForbiddenException;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.KeywordMappingRepository;
import com.example.app.repository.MentorProfileRepository;
import com.example.app.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MentorRecommendationService {

    private final KeywordMappingRepository keywordMappingRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final UserRepository userRepository;
    private final AiKeywordClient aiKeywordClient;

    public MentorRecommendationService(KeywordMappingRepository keywordMappingRepository,
                                       MentorProfileRepository mentorProfileRepository,
                                       UserRepository userRepository,
                                       AiKeywordClient aiKeywordClient) {
        this.keywordMappingRepository = keywordMappingRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.userRepository = userRepository;
        this.aiKeywordClient = aiKeywordClient;
    }

    @Transactional(readOnly = true)
    public MentorRecommendationResponse recommendMentors(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        if (user.getRole() != Role.MENTEE) {
            throw new ForbiddenException("MENTEE role required");
        }

        List<KeywordMapping> mappings = keywordMappingRepository.findTop5ByUser_IdOrderByIdAsc(userId);
        List<String> keywords = mappings.stream()
                .map(mapping -> mapping.getKeyword().getName())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toList());

        if (keywords.isEmpty()) {
            return new MentorRecommendationResponse(List.of());
        }

        List<AiKeywordClient.AiMentorScore> aiResults = new ArrayList<>(aiKeywordClient.recommendMentors(keywords));
        aiResults.sort(Comparator.comparing(AiKeywordClient.AiMentorScore::getTotalScore,
                Comparator.nullsLast(Comparator.reverseOrder())));

        List<Long> mentorIds = aiResults.stream()
                .map(AiKeywordClient.AiMentorScore::getMentorId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, MentorProfile> profileMap = new HashMap<>();
        if (!mentorIds.isEmpty()) {
            for (MentorProfile profile : mentorProfileRepository.findByUserIdIn(mentorIds)) {
                profileMap.put(profile.getUserId(), profile);
            }
        }

        Map<Long, User> userMap = new HashMap<>();
        if (!mentorIds.isEmpty()) {
            for (User mentorUser : userRepository.findAllById(mentorIds)) {
                userMap.put(mentorUser.getId(), mentorUser);
            }
        }

        List<MentorRecommendationItem> items = new ArrayList<>();
        for (AiKeywordClient.AiMentorScore score : aiResults) {
            Long mentorId = score.getMentorId();
            if (mentorId == null) {
                continue;
            }
            MentorProfile profile = profileMap.get(mentorId);
            User mentorUser = userMap.get(mentorId);

            String mentorName = mentorUser != null ? mentorUser.getName() : score.getMentorName();
            items.add(new MentorRecommendationItem(
                    mentorId,
                    mentorName,
                    profile != null ? profile.getPrice() : null,
                    profile != null ? profile.getCompany() : null,
                    profile != null ? profile.getGithubUrl() : null
            ));
        }

        if (items.size() > 5) {
            items = items.subList(0, 5);
        }

        return new MentorRecommendationResponse(items);
    }
}
