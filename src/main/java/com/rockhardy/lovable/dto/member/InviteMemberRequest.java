package com.rockhardy.lovable.dto.member;

import com.rockhardy.lovable.Enum.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @Email @NotBlank String email,
       @NotNull ProjectRole role
) {
}
