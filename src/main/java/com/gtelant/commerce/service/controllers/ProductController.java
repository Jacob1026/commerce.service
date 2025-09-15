package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.ProductRequest;
import com.gtelant.commerce.service.dtos.ProductResponse;
import com.gtelant.commerce.service.mappers.ProductMapper;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Operation(summary = "新增商品", description = "建立一個新商品並指定其分類")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        Product createdProduct = productService.createProduct(product, productRequest.getCategoryId());

        if (createdProduct == null) {
            // 可能因為 categoryId 不存在，回傳 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        ProductResponse dto = productMapper.toProductResponse(createdProduct);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "用 ID 查詢商品", description = "取得指定 ID 的商品詳細資訊")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        ProductResponse dto = productMapper.toProductResponse(product);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "商品列表、搜尋與分頁、庫存查詢", description = "可選填name或categoryId進行篩選、查詢庫存>=maxstock 或 <=minstock")
    @GetMapping("/page")
    public List<ProductResponse> searchProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer minstock,
            @RequestParam(required = false) Integer maxstock
    ) {
        // Spring Data JPA 的頁碼從 0 開始
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productService.searchProducts(name, categoryId, minstock, maxstock,pageable);
        return productPage.getContent().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Operation(summary = "更新商品資訊", description = "更新指定 ID 的商品")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
        Product productDetails = productMapper.toProduct(productRequest);
        Product updatedProduct = productService.updateProduct(id, productDetails, productRequest.getCategoryId());

        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }

        ProductResponse dto = productMapper.toProductResponse(updatedProduct);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "刪除商品", description = "刪除指定 ID 的商品")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}