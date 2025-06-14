package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.AuthResponseJWT;
import com.letrasypapeles.backend.entity.LoginRequest;
import com.letrasypapeles.backend.entity.RegisterRequest;
import com.letrasypapeles.backend.service.AuthServiceJWT;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceJWT authServiceJWT;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_shouldReturnAuthResponseJWT() throws Exception {
        AuthResponseJWT mockResponse = new AuthResponseJWT("mockToken");
        Mockito.when(authServiceJWT.login(any(LoginRequest.class))).thenReturn(mockResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("pass");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"));
    }

    @Test
    void register_shouldReturnAuthResponseJWT() throws Exception {
        AuthResponseJWT mockResponse = new AuthResponseJWT("mockRegisterToken");
        Mockito.when(authServiceJWT.register(any(RegisterRequest.class))).thenReturn(mockResponse);

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("newuser")
                .password("newpass")
                .firstname("John")
                .lastname("Doe")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockRegisterToken"));
    }
}
