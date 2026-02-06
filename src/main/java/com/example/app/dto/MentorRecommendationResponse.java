package com.example.app.dto;

import java.util.List;

public class MentorRecommendationResponse {

    private List<MentorRecommendationItem> top5;

    public MentorRecommendationResponse(List<MentorRecommendationItem> top5) {
        this.top5 = top5;
    }

    public List<MentorRecommendationItem> getTop5() {
        return top5;
    }
}
