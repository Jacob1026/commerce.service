package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.UserSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSegmentRepository extends JpaRepository<UserSegment,Integer> {
}
