package com.rockhardy.lovable.dto.member;

import com.rockhardy.lovable.Enum.ProjectRole;

public record UpdateMemberRoleRequest(
        ProjectRole role
) {
}
