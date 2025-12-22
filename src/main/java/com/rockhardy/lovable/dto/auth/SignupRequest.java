package com.rockhardy.lovable.dto.auth;

public record SignupRequest(
        String name,
        String email,
        String password
        ) {
}
