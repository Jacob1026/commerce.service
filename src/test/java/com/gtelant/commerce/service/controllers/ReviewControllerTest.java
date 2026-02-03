package com.gtelant.commerce.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtelant.commerce.service.dtos.ReviewRequest;
import com.gtelant.commerce.service.dtos.ReviewResponse;
import com.gtelant.commerce.service.services.JwtService;
import com.gtelant.commerce.service.services.ReviewService;
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


@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false) // 關閉 Security Filter
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void createReviewSuccess() throws Exception {
        // 準備請求資料 (前端傳來的 Payload)
        ReviewRequest request = new ReviewRequest();
        request.setProductId(1);
        request.setUserId(1);
        request.setRating(5);
        request.setComment("Great product!");

        // 準備預期回傳資料 (Service 回傳的 DTO)
        ReviewResponse mockResponse = new ReviewResponse();
        mockResponse.setId(1);
        mockResponse.setRating(5);
        mockResponse.setComment("Great product!");

        when(reviewService.createReview(any(ReviewRequest.class))).thenReturn(mockResponse);

        //執行請求與驗證
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 驗證 HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1)) // 驗證回傳 JSON 欄位
                .andExpect(jsonPath("$.comment").value("Great product!"));
    }
}