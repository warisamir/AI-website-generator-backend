package com.rockhardy.lovable.dto.subscription;

public record UsageTodayResponse (
    int tokensUsed,
    int tokensLimit,
    int previewsRunning,
    int previewLimit
){
}
