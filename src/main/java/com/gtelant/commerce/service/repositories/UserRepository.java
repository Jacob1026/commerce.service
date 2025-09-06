package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
