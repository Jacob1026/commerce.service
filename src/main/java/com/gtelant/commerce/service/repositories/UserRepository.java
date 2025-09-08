package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// JpaSpecificationExecutor<User> 支援多條件查詢
public interface UserRepository extends JpaRepository<User,Integer> , JpaSpecificationExecutor<User> {
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

}
