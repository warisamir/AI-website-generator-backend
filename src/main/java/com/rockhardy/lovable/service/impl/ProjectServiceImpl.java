package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.Enum.ProjectRole;
import com.rockhardy.lovable.dto.project.ProjectRequest;
import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;
import com.rockhardy.lovable.entity.Project;
import com.rockhardy.lovable.entity.ProjectMember;
import com.rockhardy.lovable.entity.ProjectMemberId;
import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.exception.IllegalStateException;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.mapper.ProjectMapper;
import com.rockhardy.lovable.repository.ProjectMemberRepository;
import com.rockhardy.lovable.repository.ProjectRepository;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.ProjectService;
import com.rockhardy.lovable.service.ProjectTemplateService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ProjectServiceImpl  implements ProjectService {
    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    ProjectTemplateService projectTemplateService;
    AuthUtils authUtils;
    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId= authUtils.getCurrentUserId();
        var projects= projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toListOfProjectSummaryResponse(projects) ;
    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectResponse getProjectById(Long projectId) {
        Long userId= authUtils.getCurrentUserId();
        Project project= getAccessibleProjectById(userId,projectId);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        Long userId= authUtils.getCurrentUserId();
        User owner=userRepository.getReferenceById(userId);
//                userRepository
//                .findById(userId)
//                .orElseThrow(()->new RuntimeException("User not found with this id"+userId));
        Project project=Project.builder()
                .name(projectRequest.name())
                .isPublic(false)
                .build();
        project=projectRepository.save(project);
        ProjectMemberId projectMemberId=new ProjectMemberId(project.getId(), owner.getId());
        ProjectMember projectMember=ProjectMember.builder()
                .projectRole(ProjectRole.OWNER)
                .user(owner)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .project(project)
                .id(projectMemberId)
                .build();
        projectMemberRepository.save(projectMember);
        projectTemplateService.initializeProjectFromTemaplate(project.getId());
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.hasPermission(#projectId)")
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        Long userId= authUtils.getCurrentUserId();
        Project project=getAccessibleProjectById(projectId,userId);
        project.setName(request.name());
        project=projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#projectId)")
    public void softDelete(Long projectId)  {
        Long userId= authUtils.getCurrentUserId();
        Project project=getAccessibleProjectById(projectId,userId);
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }
    public Project getAccessibleProjectById(Long projectId,  Long userId){
        return projectRepository.findAccessibleProjectById(projectId,userId)
                .orElseThrow(()->{
                    throw new ResourceNotFoundException("Resource not Found");
                });
    }
}
