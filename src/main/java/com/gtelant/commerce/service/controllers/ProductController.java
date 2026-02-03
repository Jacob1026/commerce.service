package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.ApiResponse;
import com.gtelant.commerce.service.dtos.ProductRequest;
import com.gtelant.commerce.service.dtos.ProductResponse;
import com.gtelant.commerce.service.mappers.ProductMapper;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@SecurityRequirement( name = "bearerAuth")
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;


    @Operation(summary = "新增商品", description = "建立一個新商品並指定其分類")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        Product createdProduct = productService.createProduct(product, productRequest.getCategoryId());
        ProductResponse dto = productMapper.toProductResponse(createdProduct);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @Operation(summary = "用 ID 查詢商品", description = "取得指定 ID 的商品詳細資訊")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductResponse(product)));
    }

    @Operation(summary = "商品列表、搜尋與分頁、庫存查詢", description = "可選填name或categoryId進行篩選、查詢庫存>=stockFrom 或 <=stockTo")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer stockFrom,
            @RequestParam(required = false) Integer stockTo
    ) {
        // Spring Data JPA 的頁碼從 0 開始
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productService.searchProducts(name, categoryId, stockFrom, stockTo, pageable);
        List<ProductResponse> dtoList = productPage.getContent().stream()
                .map(productMapper::toProductResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }

    @Operation(summary = "更新商品資訊", description = "更新指定 ID 的商品")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
        Product productDetails = productMapper.toProduct(productRequest);
        Product updatedProduct = productService.updateProduct(id, productDetails, productRequest.getCategoryId());
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductResponse(updatedProduct)));
    }

    @Operation(summary = "刪除商品", description = "刪除指定 ID 的商品")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}