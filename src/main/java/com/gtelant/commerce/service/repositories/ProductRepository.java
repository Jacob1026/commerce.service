package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
