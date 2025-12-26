package com.rockhardy.lovable.dto.auth;

public record UserProfileResponse(
        Long id,
        String email,
        String name,
        String avartar
) {
}
