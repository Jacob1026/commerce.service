package com.gtelant.commerce.service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 用於接收新增或更新 Product (商品) 的請求資料
 * This DTO is used to receive data for creating or updating a Product.
 */
@Data
public class ProductRequest {
    @NotBlank(message = "商品名稱不能空白")
    private String name;

    @NotBlank(message = "商品描述不能空白")
    private String description;

    @NotNull(message = "商品價格不能空白")
    @Min(value = 0, message = "商品價格不能小於0")
    private BigDecimal price;

    @NotNull(message = "售價不能空白")
    @Min(value = 0, message = "售價不能小於0")
    private BigDecimal salePrice;

    private Double width;
    private Double height;

    @NotNull(message = "庫存不能空白")
    @Min(value = 0, message = "庫存不能小於0")
    private Integer stock;

    /**
     * 要關聯到的商品分類 ID
     * The ID of the category to associate this product with.
     */
    private Integer categoryId;
}