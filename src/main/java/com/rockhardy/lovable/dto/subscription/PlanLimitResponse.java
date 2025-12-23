package com.rockhardy.lovable.dto.subscription;

public record PlanLimitResponse(
        String planName,
        int maxTokenPerDay,
        int maxProjects,
        boolean unlimitedAi
) {
}
