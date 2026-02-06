package com.example.app.controller;

import com.example.app.dto.MenteeProfileCreateRequest;
import com.example.app.dto.MenteeProfileCreateResponse;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.exception.AuthRequiredException;
import com.example.app.exception.ForbiddenException;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.UserRepository;
import com.example.app.service.MenteeProfileService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentees")
public class MenteeController {

    private final MenteeProfileService menteeProfileService;
    private final UserRepository userRepository;

    public MenteeController(MenteeProfileService menteeProfileService, UserRepository userRepository) {
        this.menteeProfileService = menteeProfileService;
        this.userRepository = userRepository;
    }

    @PostMapping("/portfolio")
    @ResponseStatus(HttpStatus.CREATED)
    public MenteeProfileCreateResponse createProfile(@Valid @RequestBody MenteeProfileCreateRequest request,
                                                     HttpSession session) {
        Object sessionUserId = session.getAttribute("USER_ID");
        if (sessionUserId == null) {
            throw new AuthRequiredException("Login required");
        }
        Long userId = (Long) sessionUserId;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        if (user.getRole() != Role.MENTEE) {
            throw new ForbiddenException("MENTEE role required");
        }
        return menteeProfileService.upsertPortfolio(userId, request.getPortfolioText());
    }
}
