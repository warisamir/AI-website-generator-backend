package com.rockhardy.lovable.dto.member;

import com.rockhardy.lovable.Enum.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String avatarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
