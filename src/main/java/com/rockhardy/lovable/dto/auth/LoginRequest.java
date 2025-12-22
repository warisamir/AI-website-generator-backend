package com.rockhardy.lovable.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
