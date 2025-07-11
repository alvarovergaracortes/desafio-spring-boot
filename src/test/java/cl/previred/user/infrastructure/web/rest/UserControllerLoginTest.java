package cl.previred.user.infrastructure.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.previred.common.security.PasswordUtil;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.persistence.repository.UserJpaRepository;
import cl.previred.user.infrastructure.web.dto.LoginRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerLoginTest {
	@Autowired 
	private MockMvc mockMvc;
    
	@Autowired
    private ObjectMapper objectMapper;
    
    @Autowired 
    private UserJpaRepository userJpaRepository;
    
    @Autowired
    private PasswordUtil passwordUtil;

    @BeforeEach
    void setUp() {
        userJpaRepository.findByUsername("testuser")
            .orElseGet(() -> {
                UserEntity user = new UserEntity();
                user.setUsername("testuser");
                user.setPassword(passwordUtil.encode("123456"));
                user.setRoles("ADMIN");
                return userJpaRepository.save(user);
            });
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        var loginRequest = new LoginRequest("testuser", "123456", "ADMIN");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldFailLoginWithInvalidPassword() throws Exception {
        var loginRequest = new LoginRequest("testuser", "wrongpassword", "USER");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnNotFoundForUnknownUser() throws Exception {
        var loginRequest = new LoginRequest("unknownuser", "123456", "USER");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForMissingUsername() throws Exception {
        var loginRequest = new LoginRequest("", "123456", "USER");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForMissingPassword() throws Exception {
        var loginRequest = new LoginRequest("testuser", "", "USER");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest());
    }
}
