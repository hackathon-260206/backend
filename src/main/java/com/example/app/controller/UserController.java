package com.example.app.controller;

import com.example.app.dto.UserLoginRequest;
import com.example.app.dto.UserLoginResponse;
import com.example.app.dto.UserLogoutResponse;
import com.example.app.dto.UserResponse;
import com.example.app.dto.UserSignupRequest;
import com.example.app.dto.UserSignupResponse;
import com.example.app.dto.SessionStatusResponse;
import com.example.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSignupResponse signup(@Valid @RequestBody UserSignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public UserLoginResponse login(@Valid @RequestBody UserLoginRequest request, HttpSession session) {
        UserLoginResponse response = userService.login(request);
        session.setAttribute("USER_ID", response.getId());
        return response;
    }

    @PostMapping("/logout")
    public UserLogoutResponse logout(HttpSession session) {
        session.invalidate();
        return new UserLogoutResponse("로그아웃 되었습니다.");
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/me")
    public UserResponse me(HttpSession session) {
        Object sessionUserId = session.getAttribute("USER_ID");
        if (!(sessionUserId instanceof Long)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return userService.findById((Long) sessionUserId);
    }

    @GetMapping("/session")
    public SessionStatusResponse sessionStatus(HttpSession session) {
        Object sessionUserId = session.getAttribute("USER_ID");
        if (sessionUserId instanceof Long) {
            return new SessionStatusResponse(true, (Long) sessionUserId);
        }
        return new SessionStatusResponse(false, null);
    }

    @GetMapping("/me/profile")
    public com.example.app.dto.UserProfileResponse getMyProfile(HttpSession session) {
        Object sessionUserId = session.getAttribute("USER_ID");
        if (!(sessionUserId instanceof Long)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return userService.getUserProfile((Long) sessionUserId);
    }
}
