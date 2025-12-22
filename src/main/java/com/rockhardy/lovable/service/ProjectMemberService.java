package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.member.MemberResponse;
import com.rockhardy.lovable.entity.ProjectMember;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {



    List<ProjectMember> getProjectMembers(Long projectId, Long userId);

}
