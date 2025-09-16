package com.gtelant.commerce.service.models;

import com.gtelant.commerce.service.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 一則評論屬於一個使用者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false) // 指定外鍵欄位
    private User user;

    // 一則評論屬於一個商品
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // 指定外鍵欄位
    private Product product;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "review_status")
    private ReviewStatus reviewStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 在新增資料時，自動設定 createdAt 的時間
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;

}
