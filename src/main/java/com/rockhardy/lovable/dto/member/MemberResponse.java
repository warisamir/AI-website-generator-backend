package com.rockhardy.lovable.dto.member;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String avatarUrl,
        ProjectRole role,
        instant invite,
) {
}
