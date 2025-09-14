package com.gtelant.commerce.service.dtos;

import lombok.Data;

/**
 * 用於接收新增或更新 Category 的請求資料
 */
@Data
public class CategoryRequest {
    private String name;
}