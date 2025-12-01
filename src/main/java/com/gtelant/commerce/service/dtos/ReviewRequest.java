package com.gtelant.commerce.service.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @Min(value = 1, message = "評分最低為 1 分")
    @Max(value = 5, message = "評分最高為 5 分")
    private Integer rating; // 評分

    private String comment; // 評論內容
    private Integer userId; // 發表評論的使用者 ID

    @NotNull(message = "商品 ID 不能為空")
    private Integer productId; // 被評論的商品 ID
}
