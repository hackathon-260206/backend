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

    public MentorRecommendationService(KeywordMappingRepository keywordMappingRepository,
                                       MentorProfileRepository mentorProfileRepository,
                                       UserRepository userRepository) {
        this.keywordMappingRepository = keywordMappingRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.userRepository = userRepository;
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
            return new MentorRecommendationResponse(buildFallbackRecommendations());
        }

        // AI 연동 중단: 보유 데이터 기준으로 추천
        return new MentorRecommendationResponse(buildFallbackRecommendations());
    }

    private List<MentorRecommendationItem> buildFallbackRecommendations() {
        List<MentorProfile> profiles = new ArrayList<>(mentorProfileRepository.findAll());
        if (profiles.isEmpty()) {
            return List.of();
        }
        profiles.sort(Comparator.comparing(MentorProfile::getUserId,
                Comparator.nullsLast(Comparator.naturalOrder())));
        if (profiles.size() > 5) {
            profiles = profiles.subList(0, 5);
        }

        List<Long> mentorIds = profiles.stream()
                .map(MentorProfile::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, User> userMap = new HashMap<>();
        if (!mentorIds.isEmpty()) {
            for (User mentorUser : userRepository.findAllById(mentorIds)) {
                userMap.put(mentorUser.getId(), mentorUser);
            }
        }

        List<MentorRecommendationItem> items = new ArrayList<>();
        for (MentorProfile profile : profiles) {
            Long mentorId = profile.getUserId();
            if (mentorId == null) {
                continue;
            }
            User mentorUser = userMap.get(mentorId);
            String mentorName = mentorUser != null ? mentorUser.getName() : "Mentor " + mentorId;
            items.add(new MentorRecommendationItem(
                    mentorId,
                    mentorName,
                    profile.getPrice(),
                    profile.getCompany(),
                    profile.getGithubUrl()
            ));
        }

        return items;
    }
}
