package com.gtelant.commerce.service.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_thumbnail")
    private String imageThumbnail;

    // 多張圖片對應到一個商品
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // 指定外鍵
    private Product product;
}