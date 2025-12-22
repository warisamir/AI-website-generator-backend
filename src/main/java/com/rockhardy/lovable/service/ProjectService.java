package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.project.ProjectRequest;
import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;


import java.util.List;

public interface ProjectService {
     List<ProjectSummaryResponse> getUserProjects(Long userId);
     ProjectSummaryResponse getProjectById(Long userId, Long id);
     ProjectResponse createProject(ProjectRequest projectRequest, Long userId);
     ProjectResponse updateProject(Long id, Long userId, ProjectRequest request);
     void softDelete(Long id, Long userId);
}
