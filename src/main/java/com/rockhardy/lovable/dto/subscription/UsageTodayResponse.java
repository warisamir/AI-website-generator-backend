package com.rockhardy.lovable.dto.subscription;

public record UsageTodayResponse (
        Integer tokensUsed,
        Integer tokensLimit,
        Integer previewsRunning,
        Integer previewLimit
){
}
