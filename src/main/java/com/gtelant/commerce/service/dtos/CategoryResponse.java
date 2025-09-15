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
    private List<ProductSimpleResponse> products;

}