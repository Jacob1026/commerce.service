package com.gtelant.commerce.service.dtos;


import lombok.Data;

@Data
public class ReviewRequest {

    private Integer rating; // 評分
    private String comment; // 評論內容
    private Integer userId; // 發表評論的使用者 ID
    private Integer productId; // 被評論的商品 ID
}
