package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.project.ProjectRequest;
import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;
import com.rockhardy.lovable.entity.Project;
import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.mapper.ProjectMapper;
import com.rockhardy.lovable.repository.ProjectRepository;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        var projects= projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toListOfProjectSummaryResponse(projects) ;
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {
        Project project= getAccessibleProjectById(id,userId);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest, Long userId) {
        User owner=userRepository
                .findById(userId)
                .orElseThrow(()->new RuntimeException("User not found with this id"+userId));
        Project project=Project.builder()
                .name(projectRequest.name())
                .owner(owner)
                .isPublic(false)
                .build();
        project=projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project=getAccessibleProjectById(id,userId);
        project.setName(request.name());
        project=projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
//        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project=getAccessibleProjectById(id,userId);
        if(!project.getOwner().getId().equals(userId)){
            throw  new RuntimeException("you are not allow to delete");
        }
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }
    public Project getAccessibleProjectById(Long projectId,  Long userId){
        return projectRepository.findAccessibleProjectById(projectId,userId).orElseThrow();
    }
}
