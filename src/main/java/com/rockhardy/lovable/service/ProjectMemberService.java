package com.rockhardy.lovable.service;
import com.rockhardy.lovable.dto.member.InviteMemberRequest;
import com.rockhardy.lovable.dto.member.MemberResponse;
import com.rockhardy.lovable.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
     List<ProjectMember> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, Long userId, InviteMemberRequest request);
}
