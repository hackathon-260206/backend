package com.example.app.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class AiKeywordClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final boolean enabled;
    private static final int MAX_KEYWORDS = 5;
    private static final Pattern TOKEN_SPLIT = Pattern.compile("[^\\p{L}\\p{Nd}]+");
    private static final Set<String> STOPWORDS = Set.of(
            "the", "a", "an", "and", "or", "is", "are", "to", "of", "in", "on", "for",
            "with", "at", "from", "by", "this", "that", "it", "as", "be"
    );

    public AiKeywordClient(@Value("${ai.base-url:}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.enabled = baseUrl != null && !baseUrl.isBlank();
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<String> extractKeywords(String text) {
        if (!enabled) {
            return fallbackKeywords(text);
        }
        try {
            AiKeywordResponse response = restClient.post()
                    .uri("/analyze")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AiKeywordRequest(text))
                    .retrieve()
                    .body(AiKeywordResponse.class);
            if (response == null || response.getKeywords() == null) {
                return fallbackKeywords(text);
            }
            return response.getKeywords();
        } catch (RestClientException ex) {
            return fallbackKeywords(text);
        }
    }

    public List<AiMentorScore> recommendMentors(List<String> keywords) {
        if (!enabled) {
            return List.of();
        }
        try {
            AiRecommendResponse response = restClient.post()
                    .uri("/recommend")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AiRecommendRequest(keywords))
                    .retrieve()
                    .body(AiRecommendResponse.class);
            if (response == null || response.getTop5() == null) {
                return List.of();
            }
            return response.getTop5();
        } catch (RestClientException ex) {
            return List.of();
        }
    }

    private List<String> fallbackKeywords(String text) {
        if (text == null || text.isBlank()) {
            return List.of("mentoring", "backend", "spring", "api", "career");
        }
        String[] tokens = TOKEN_SPLIT.split(text);
        Set<String> unique = new LinkedHashSet<>();
        for (String raw : tokens) {
            if (raw == null) {
                continue;
            }
            String token = raw.trim().toLowerCase(Locale.ROOT);
            if (token.length() < 2 || STOPWORDS.contains(token)) {
                continue;
            }
            unique.add(raw.trim());
            if (unique.size() >= MAX_KEYWORDS) {
                break;
            }
        }
        if (unique.isEmpty()) {
            return List.of("mentoring", "backend", "spring", "api", "career");
        }
        return new ArrayList<>(unique);
    }

    private static class AiKeywordRequest {
        private final String text;

        private AiKeywordRequest(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    private static class AiRecommendRequest {
        private final List<String> keywords;

        private AiRecommendRequest(List<String> keywords) {
            this.keywords = keywords;
        }

        public List<String> getKeywords() {
            return keywords;
        }
    }

    private static class AiKeywordResponse {
        private List<String> keywords;

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }
    }

    public static class AiMentorScore {
        @JsonProperty("mentor_id")
        private Long mentorId;
        @JsonProperty("mentor_name")
        private String mentorName;
        @JsonProperty("total_score")
        private Double totalScore;
        private Double topicMatch;
        private Double stackMatch;
        private Double quality;

        public Long getMentorId() {
            return mentorId;
        }

        public String getMentorName() {
            return mentorName;
        }

        public Double getTotalScore() {
            return totalScore;
        }

        public Double getTopicMatch() {
            return topicMatch;
        }

        public Double getStackMatch() {
            return stackMatch;
        }

        public Double getQuality() {
            return quality;
        }

        public void setMentorId(Long mentorId) {
            this.mentorId = mentorId;
        }

        public void setMentorName(String mentorName) {
            this.mentorName = mentorName;
        }

        public void setTotalScore(Double totalScore) {
            this.totalScore = totalScore;
        }

        public void setTopicMatch(Double topicMatch) {
            this.topicMatch = topicMatch;
        }

        public void setStackMatch(Double stackMatch) {
            this.stackMatch = stackMatch;
        }

        public void setQuality(Double quality) {
            this.quality = quality;
        }
    }

    private static class AiRecommendResponse {
        @JsonProperty("normalized_user")
        private Object normalizedUser;
        private List<AiMentorScore> top5;
        private Object fallback;

        public Object getNormalizedUser() {
            return normalizedUser;
        }

        public List<AiMentorScore> getTop5() {
            return top5;
        }

        public Object getFallback() {
            return fallback;
        }

        public void setNormalizedUser(Object normalizedUser) {
            this.normalizedUser = normalizedUser;
        }

        public void setTop5(List<AiMentorScore> top5) {
            this.top5 = top5;
        }

        public void setFallback(Object fallback) {
            this.fallback = fallback;
        }
    }
}
