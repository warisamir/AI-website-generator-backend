package com.rockhardy.lovable.service;
import com.rockhardy.lovable.dto.member.InviteMemberRequest;
import com.rockhardy.lovable.dto.member.MemberResponse;
import com.rockhardy.lovable.dto.member.UpdateMemberRoleRequest;
import com.rockhardy.lovable.entity.ProjectMember;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
     List<MemberResponse> getProjectMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request);

    void removeMember(Long projectId, Long memberId);
}
