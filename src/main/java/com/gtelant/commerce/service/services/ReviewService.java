package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.dtos.ReviewRequest;
import com.gtelant.commerce.service.dtos.ReviewResponse;
import com.gtelant.commerce.service.enums.ReviewStatus;
import com.gtelant.commerce.service.mappers.ReviewMapper;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.models.Review;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.repositories.ProductRepository;
import com.gtelant.commerce.service.repositories.ReviewRepository;
import com.gtelant.commerce.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository,
                         ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    // 新增評論
    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->  new RuntimeException ("找不到對應的商品，ID: " + request.getProductId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("找不到對應的用戶，ID: " + request.getUserId()));

        Review review = reviewMapper.toReview(request);

        review.setProduct(product);
        review.setUser(user);
        review.setReviewStatus(ReviewStatus.PENDING);

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toReviewResponse(savedReview);
    }

// 根據 ID 查詢評論
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Integer id) {
        Review review = reviewRepository.findByIdWithProductAndUser(id)
                .orElseThrow(() -> new RuntimeException ("找不到對應的評論，ID: " + id));
        return reviewMapper.toReviewResponse(review);
    }
// 根據商品 ID 查詢評論，並支援分頁
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByProductId(Integer productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("找不到對應的商品，ID: " + productId);
        }
        Page<Review> reviewPage = reviewRepository.findById(productId,pageable);
        return reviewPage.map(reviewMapper::toReviewResponse);
    }

    @Transactional
    public ReviewResponse updateReview(Integer id, ReviewRequest request) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到要更新的評論，ID: " + id));

        existingReview.setRating(request.getRating());
        existingReview.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(existingReview);
        return reviewMapper.toReviewResponse(updatedReview);
    }

    @Transactional
    public void deleteReview(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("找不到要刪除的評論，ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Transactional
    public List<ReviewResponse> createReviews(List<ReviewRequest> requests) {
        List<Review> reviewsToSave = new ArrayList<>();

        // 遍歷所有請求，驗證並轉換成 Review Entity
        for (ReviewRequest request : requests) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("找不到對應的商品，ID: " + request.getProductId()));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("找不到對應的用戶，ID: " + request.getUserId()));

            Review review = reviewMapper.toReview(request);
            review.setProduct(product);
            review.setUser(user);
            review.setCreatedAt(LocalDateTime.now());
            review.setReviewStatus(ReviewStatus.PENDING); // 預設為待審核狀態

            reviewsToSave.add(review);
        }

        // 使用 saveAll 一次性將所有評論儲存到資料庫，效率更高
        List<Review> savedReviews = reviewRepository.saveAll(reviewsToSave);

        // 將儲存後的 Entities 轉換為 Response DTOs 並回傳
        return savedReviews.stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReviewsByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            // 如果傳入的列表是空的，就直接返回，不做任何事
            return;
        }
        // JpaRepository 提供了批次刪除的方法，效率比一個一個刪除好
        reviewRepository.deleteAllByIdInBatch(ids);
    }

}