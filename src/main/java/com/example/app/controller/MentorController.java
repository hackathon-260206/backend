package com.example.app.controller;

import com.example.app.dto.MentorProfileCreateRequest;
import com.example.app.dto.MentorProfileCreateResponse;
import com.example.app.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public MentorProfileCreateResponse createProfile(@Valid @RequestBody MentorProfileCreateRequest request, jakarta.servlet.http.HttpSession session) {
        Object userIdObj = session.getAttribute("USER_ID");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long userId = (Long) userIdObj;
        return mentorService.createProfile(userId, request);
    }
}
