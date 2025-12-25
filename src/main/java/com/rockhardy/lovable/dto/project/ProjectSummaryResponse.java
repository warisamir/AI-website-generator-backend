package com.rockhardy.lovable.dto.project;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long projectId,
        String projectName  ,
        Instant createdAt,
        Instant updatedAt
) {
}
