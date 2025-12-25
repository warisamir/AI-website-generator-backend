package com.rockhardy.lovable.dto.subscription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokenPerDay,
        Integer maxProjects,
        boolean unlimitedAi
) {
}
