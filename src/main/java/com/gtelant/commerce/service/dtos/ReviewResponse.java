package com.gtelant.commerce.service.dtos;

import com.gtelant.commerce.service.enums.ReviewStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Integer id;
    private UserSimpleResponse user;
    private ProductSimpleResponse product;
    private Integer rating;
    private String comment;
    private ReviewStatus reviewStatus; // <-- 建議使用 Enum 型別而非 String
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt; // <-- 欄位名稱建議改為 deletedAt
}