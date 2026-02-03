package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.ReviewRequest;
import com.gtelant.commerce.service.dtos.ReviewResponse; // <-- 修改返回類型
import com.gtelant.commerce.service.dtos.UpdateReviewStatusRequest;

import com.gtelant.commerce.service.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // 引入 @RequestBody

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
@CrossOrigin("*")
@SecurityRequirement( name = "bearerAuth")
@Tag(name = "Review", description = "Review management APIs")

public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "新增顧客評論", description = "一個顧客評論一個商品")
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewResponse createdReviewResponse = reviewService.createReview(reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewResponse);
    }

    @Operation(summary = "批次新增顧客評論", description = "一次新增多筆商品評論")
    @PostMapping("/batch")
    public ResponseEntity<List<ReviewResponse>> createReviews(@RequestBody List<ReviewRequest> reviewRequests) {
        List<ReviewResponse> createdReviews = reviewService.createReviews(reviewRequests);
        // 批次建立成功，回傳 201 Created 狀態碼以及新增的評論列表
        return new ResponseEntity<>(createdReviews, HttpStatus.CREATED);
    }

    @Operation(summary = "批次刪除顧客評論", description = "根據 ID 列表一次刪除多筆評論")
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteReviews(@RequestBody List<Integer> reviewIds) {
        reviewService.deleteReviewsByIds(reviewIds);
        // 批次刪除成功，回傳 204 No Content 狀態碼，表示成功但沒有內容返回
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "批次更新評論狀態", description = "用ID列表更新多筆評論的狀態")
    @PutMapping("/status")
    public ResponseEntity<List<ReviewResponse>> updateReviewStatus(@RequestBody UpdateReviewStatusRequest request) {
        try {
            // 1. 正確接收 Service 回傳的 DTO 列表
            List<ReviewResponse> updatedReviewResponses = reviewService.updateStatusForIds(
                    request.getIds(),
                    request.getReviewStatus()
            );
            return ResponseEntity.ok(updatedReviewResponses);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
