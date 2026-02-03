package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.exceptions.ResourceNotFoundException;
import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.repositories.CategoryRepository;
import com.gtelant.commerce.service.repositories.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    //新增商品
    public Product createProduct(Product product, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + categoryId));

        product.setCategory(category);
        return productRepository.save(product);
    }

    //更新商品
    public Product updateProduct(Integer productId, Product productDetails, Integer categoryId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在，ID: " + productId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + categoryId));

        productDetails.setId(existingProduct.getId());
        productDetails.setCategory(category);
        return productRepository.save(productDetails);
    }

   //刪除商品
    public void deleteProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("無法刪除，商品不存在，ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    //根據 ID 查詢單一商品
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在，ID: " + productId));
    }

    //條件查詢
    public Page<Product> searchProducts(String name, Integer categoryId, Integer stockFrom, Integer stockTo, Pageable pageable) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 如果 name 存在且不為空，加入名稱模糊查詢
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            // 如果 categoryId 存在，加入分類查詢
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            //庫存查詢

            if (stockFrom != null && stockTo == null) {
                // WHERE stock >= stockFrom
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), stockFrom));

            }else if (stockFrom == null && stockTo != null) {
                // WHERE stock <= stockTo
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stock"), stockTo));

            } else if (stockFrom != null && stockTo != null) {
                // WHERE stock >= stockFrom
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), stockFrom));
                // WHERE stock <= stockTo
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stock"), stockTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec, pageable);
    }
}