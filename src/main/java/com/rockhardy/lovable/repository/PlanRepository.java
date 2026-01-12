package com.rockhardy.lovable.repository;

import com.rockhardy.lovable.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,Long> {
}
