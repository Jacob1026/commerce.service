package com.gtelant.commerce.service.repositories;

import com.gtelant.commerce.service.models.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<Segment,Integer> {
}
