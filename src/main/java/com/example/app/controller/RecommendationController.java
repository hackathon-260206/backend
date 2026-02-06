package com.example.app.controller;

import com.example.app.dto.MentorRecommendationResponse;
import com.example.app.exception.AuthRequiredException;
import com.example.app.service.MentorRecommendationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final MentorRecommendationService mentorRecommendationService;

    public RecommendationController(MentorRecommendationService mentorRecommendationService) {
        this.mentorRecommendationService = mentorRecommendationService;
    }

    @GetMapping("/mentors")
    @ResponseStatus(HttpStatus.OK)
    public MentorRecommendationResponse recommendMentors(HttpSession session) {
        Object sessionUserId = session.getAttribute("USER_ID");
        if (sessionUserId == null) {
            throw new AuthRequiredException("Login required");
        }
        Long userId = (Long) sessionUserId;
        return mentorRecommendationService.recommendMentors(userId);
    }
}
