package com.gtelant.commerce.service.dtos;

import lombok.Data;

@Data

public class ProductImageResponse {
    private Integer id;
    private String imageUrl;
    private String imageThumbnail;
}
