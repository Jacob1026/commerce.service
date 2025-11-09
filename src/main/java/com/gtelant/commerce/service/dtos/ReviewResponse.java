package com.gtelant.commerce.service.dtos;

import com.gtelant.commerce.service.enums.ReviewStatus;
import com.gtelant.commerce.service.models.Review;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewResponse {
    private Integer id;
    private UserSimpleResponse user;
    private ProductSimpleResponse product;
    private Integer rating;
    private String comment;
    private ReviewStatus reviewStatus;
    private LocalDateTime deletedAt;
}