package com.gtelant.commerce.service.mappers;

import com.gtelant.commerce.service.dtos.*;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.models.Review;
import com.gtelant.commerce.service.models.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toReviewResponse(Review review) {
        if (review == null) {
            return null;
        }

        ReviewResponse dto = new ReviewResponse();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewStatus(review.getReviewStatus());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setDeletedAt(review.getDeletedAt());

        // 呼叫輔助方法來轉換關聯物件
        if (review.getUser() != null) {
            dto.setUser(toUserSimpleResponse(review.getUser()));
        }
        if (review.getProduct() != null) {
            dto.setProduct(toProductSimpleResponse(review.getProduct()));
        }
        return dto;
    }

    private UserSimpleResponse toUserSimpleResponse(User user) {
            if (user == null) {
                return null;
            }
            UserSimpleResponse dto = new UserSimpleResponse();
            dto.setId(user.getId());
            // 組合 firstName 和 lastName 成為 fullName
            // 這裡做了一些空值處理，避免出現 "null" 字樣
            String firstName = user.getFirstName() != null ? user.getFirstName() : "";
            String lastName = user.getLastName() != null ? user.getLastName() : "";
            // 將名字組合起來，並用 trim() 去除前後多餘的空格
            dto.setFullName((firstName + " " + lastName).trim());

            return dto;
    }

    private ProductSimpleResponse toProductSimpleResponse(Product product) {
        if (product == null) return null;
        ProductSimpleResponse dto = new ProductSimpleResponse();
        dto.setId(product.getId());
        dto.setName(product.getName()); // 假設 Product Entity 有 getName()
        return dto;
    }

    public Review toReview(ReviewRequest request) {
        if (request == null) return null;
        Review review = new Review();
        review.setRating(request.getRating()); // 修正: request.get() -> request.getRating()
        review.setComment(request.getComment());
        return review;
    }


}