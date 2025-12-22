package com.rockhardy.lovable.dto.auth;

public record AuthResponse(
        String token,
        UserProfileResponse user) {
}
