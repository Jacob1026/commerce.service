package com.gtelant.commerce.service.dtos;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 用於接收新增或更新 Product (商品) 的請求資料
 * This DTO is used to receive data for creating or updating a Product.
 */
@Data
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Double width;
    private Double height;
    private Integer stock;

    /**
     * 要關聯到的商品分類 ID
     * The ID of the category to associate this product with.
     */
    private Integer categoryId;
}