package com.rockhardy.lovable.dto.subscription;

public record PlanResponse(
        Long id,
        String name,
        Integer maxProjects,
        Integer maxTokenPerDay,
        Boolean unlimitedAi,
        String price
) {
}
