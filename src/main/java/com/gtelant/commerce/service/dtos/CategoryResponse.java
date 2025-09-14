package com.gtelant.commerce.service.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用於向客戶端回傳 Category 的資料
 */
@Data
public class CategoryResponse {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    // 為了完整性，可以回傳該分類下的商品資訊
    // 注意：這裡應該使用 ProductResponse DTO，而不是直接用 Product Entity
    private List<ProductResponse> products;
}