package com.rockhardy.lovable.dto.member;

import com.rockhardy.lovable.Enum.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
       @NotNull ProjectRole role
) {
}
