package com.example.app.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AiKeywordClient {

    private final RestClient restClient;
    private final String baseUrl;

    public AiKeywordClient(@Value("${ai.base-url:}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<String> extractKeywords(String text) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("AI base URL is not configured");
        }
        AiKeywordResponse response = restClient.post()
                .uri("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AiKeywordRequest(text))
                .retrieve()
                .body(AiKeywordResponse.class);
        if (response == null || response.getKeywords() == null) {
            return List.of();
        }
        return response.getKeywords();
    }

    public List<AiMentorScore> recommendMentors(List<String> keywords) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("AI base URL is not configured");
        }
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
