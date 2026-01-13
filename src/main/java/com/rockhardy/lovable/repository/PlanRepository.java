package com.rockhardy.lovable.repository;

import com.rockhardy.lovable.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    Optional<Plan> findByStripePriceId(String id);
}
