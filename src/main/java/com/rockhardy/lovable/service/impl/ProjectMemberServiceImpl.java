package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.member.InviteMemberRequest;
import com.rockhardy.lovable.dto.member.MemberResponse;
import com.rockhardy.lovable.dto.member.UpdateMemberRoleRequest;
import com.rockhardy.lovable.entity.Project;
import com.rockhardy.lovable.entity.ProjectMember;
import com.rockhardy.lovable.entity.ProjectMemberId;
import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.mapper.ProjectMemberMapper;
import com.rockhardy.lovable.repository.ProjectMemberRepository;
import com.rockhardy.lovable.repository.ProjectRepository;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Builder
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final UserRepository userRepository;
    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    AuthUtils authUtils;
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId) {
        Long userId= authUtils.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);
        return projectMemberRepository.findByProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Long userId= authUtils.getCurrentUserId();
        Project project= getAccessibleProjectById(projectId,userId);
        User invitee=userRepository.findByUsername(request.username()).orElseThrow(
                ()->{
                    throw new ResourceNotFoundException("Member not found");
                }
        );
        if(invitee.getId().equals(userId)){
            throw new RuntimeException("Cannot invite yourself");
        }
            ProjectMemberId projectMemberId= new ProjectMemberId(projectId,invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw  new RuntimeException("member also exist in the group");
        }
        ProjectMember projectMember=new ProjectMember().builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        Long userId=  authUtils.getCurrentUserId();
        Project project= getAccessibleProjectById(projectId,userId);
        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,memberId);
        ProjectMember projectMember=projectMemberRepository.findById(projectMemberId)
                .orElseThrow(()-> new ResourceNotFoundException("member not found"));
        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    public void removeMember(Long projectId, Long memberId) {
        Long userId= authUtils.getCurrentUserId();
        Project project= getAccessibleProjectById(projectId,userId);
        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,memberId);
        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new ResourceNotFoundException("you are not in this project");
        }
        projectMemberRepository.deleteById(projectMemberId);
    }

    public Project getAccessibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();
    }
}
