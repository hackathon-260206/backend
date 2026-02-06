package com.example.app.service;

import com.example.app.dto.UserResponse;
import com.example.app.dto.UserLoginRequest;
import com.example.app.dto.UserLoginResponse;
import com.example.app.dto.UserSignupRequest;
import com.example.app.dto.UserSignupResponse;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.exception.UnauthorizedException;
import com.example.app.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import com.example.app.dto.UserProfileResponse;
import com.example.app.dto.MentorProfileResponse;
import com.example.app.dto.MenteeProfileResponse;
import com.example.app.entity.MentorProfile;
import com.example.app.entity.MenteeProfile;
import com.example.app.repository.MentorProfileRepository;
import com.example.app.repository.MenteeProfileRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final MenteeProfileRepository menteeProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, MentorProfileRepository mentorProfileRepository, MenteeProfileRepository menteeProfileRepository) {
        this.userRepository = userRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.menteeProfileRepository = menteeProfileRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public UserSignupResponse signup(UserSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                request.getBirthDate(),
                request.getGender(),
                request.getRole()
        );
        User saved = userRepository.save(user);
        return new UserSignupResponse(saved.getId(), "signup success");
    }

    @Transactional(readOnly = true)
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return new UserLoginResponse(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getGender(),
                user.getRole()
        );
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (user.getRole() == com.example.app.entity.Role.MENTOR) {
            MentorProfile profile = mentorProfileRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Mentor profile not found"));
            return new MentorProfileResponse(
                    user.getId(), user.getId(), user.getName(), user.getEmail(), user.getBirthDate(), user.getGender(), user.getRole(),
                    profile.getTechStack(), profile.getPrice(), profile.getCompany(), profile.getGithubUrl(), profile.getMentoringCount()
            );
        } else if (user.getRole() == com.example.app.entity.Role.MENTEE) {
            MenteeProfile profile = menteeProfileRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Mentee profile not found"));
            return new MenteeProfileResponse(
                    user.getId(), user.getId(), user.getName(), user.getEmail(), user.getBirthDate(), user.getGender(), user.getRole(),
                    profile.getTechStack(), profile.getUniversity(), profile.getPortfolioText(), profile.getGithubUrl()
            );
        } else {
             // Fallback for users without specific role profile, user UserProfileResponse base
            return new UserProfileResponse(
                    user.getId(), user.getId(), user.getName(), user.getEmail(), user.getBirthDate(), user.getGender(), user.getRole()
            );
        }
    }
}
