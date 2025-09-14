package com.gtelant.commerce.service.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用於向客戶端回傳 Product (商品) 的詳細資料
 */
@Data
public class ProductResponse {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Double width;
    private Double height;
    private Integer stock;

    // 將關聯的 Category 物件簡化為 ID 和名稱，避免巢狀過深
    private Integer categoryId;
    private String categoryName;

    // 包含商品的圖片列表，使用 ProductImageResponse DTO
    private List<ProductImageResponse> images;
}