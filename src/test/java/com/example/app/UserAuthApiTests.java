package com.example.app;

import com.example.app.entity.Gender;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserAuthApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private Long seedUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User(
                "홍길동",
                "user@example.com",
                encoder.encode("plainPassword123"),
                LocalDate.of(1995, 5, 20),
                Gender.MALE,
                Role.MENTEE
        );
        return userRepository.save(user).getId();
    }

    @Test
    void login_and_logout_success() throws Exception {
        Long userId = seedUser();

        String loginJson = """
            {
              "email": "user@example.com",
              "password": "plainPassword123"
            }
            """;

        MvcResult loginResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.role").value("MENTEE"))
                .andReturn();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(loginResult.getRequest().getSession(false))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그아웃 되었습니다."));
    }
}
