package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.project.ProjectRequest;
import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;


import java.util.List;
public interface ProjectService {
     List<ProjectSummaryResponse> getUserProjects();
     ProjectResponse getProjectById(Long id);
     ProjectResponse createProject(ProjectRequest projectRequest);
     ProjectResponse updateProject(Long id, ProjectRequest request);
     void softDelete(Long id);
}
