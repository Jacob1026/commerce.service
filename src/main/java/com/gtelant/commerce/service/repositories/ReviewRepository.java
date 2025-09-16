package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository <Review, Integer>, JpaSpecificationExecutor<Review> {
    Page<Review> findByProductId(Integer productId, Pageable pageable);
    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user LEFT JOIN FETCH r.product WHERE r.id = :id")
    Optional<Review> findByIdWithProductAndUser(Integer id);
    Page<Review> findById(Integer productId, Pageable pageable);
}
