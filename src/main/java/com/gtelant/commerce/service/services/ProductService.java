package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.models.Category;
import com.gtelant.commerce.service.models.Product;
import com.gtelant.commerce.service.repositories.CategoryRepository;
import com.gtelant.commerce.service.repositories.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 新增商品，並處理與分類的關聯
     */
    public Product createProduct(Product product, Integer categoryId) {
        // 根據 categoryId 尋找 Category Entity
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            // 如果找不到對應的分類，可以拋出例外或回傳 null
            // 這裡我們回傳 null，讓 Controller 去處理後續
            return null;
        }
        // 建立商品與分類的關聯
        product.setCategory(category.get());
        return productRepository.save(product);
    }

    /**
     * 更新商品資訊
     */
    public Product updateProduct(Integer productId, Product productDetails, Integer categoryId) {
        if (!productRepository.existsById(productId)) {
            return null; // 商品不存在
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            return null; // 分類不存在
        }
        productDetails.setId(productId); // 確保更新的是正確的商品
        productDetails.setCategory(category.get());
        return productRepository.save(productDetails);
    }

    /**
     * 刪除商品
     */
    public boolean deleteProduct(Integer productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    /**
     * 根據 ID 查詢單一商品
     */
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }

    //條件查詢
    public Page<Product> searchProducts(String name, Integer categoryId, Integer minstock, Integer maxstock, Pageable pageable) {
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
            // 如果 maxstock 存在，加入「小於等於」的庫存查詢
            if (maxstock != null) {
                // WHERE stock >= minstock
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stock"), minstock));
            }
            // 如果 minstock 存在，加入「大於等於」的庫存查詢
            if (minstock!= null) {
                // WHERE stock <= maxstock
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), maxstock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec, pageable);
    }
}