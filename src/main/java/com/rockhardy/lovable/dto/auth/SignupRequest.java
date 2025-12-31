package com.rockhardy.lovable.dto.auth;

import jakarta.validation.constraints.*;

public record SignupRequest(
        @NotBlank @Size(min=1,max=30) String name,
        @NotNull @Email String username,
        @NotBlank @Size(min=4,max=130) String password
        ) {
}
