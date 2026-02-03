package com.gtelant.commerce.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtelant.commerce.service.dtos.LoginRequest;
import com.gtelant.commerce.service.mappers.UserMapper;
import com.gtelant.commerce.service.services.JwtAuthService;
import com.gtelant.commerce.service.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JwtAuthController.class)
@AutoConfigureMockMvc(addFilters = false) // 關閉 Filter 執行，但 Config Bean 仍會被建立
class JwtAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtAuthService jwtAuthService;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void loginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "password");
        String mockToken = "mock-jwt-token-string";

        when(jwtAuthService.loginUser(any(LoginRequest.class))).thenReturn(mockToken);

        mockMvc.perform(post("/jwt/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(mockToken));
    }

}