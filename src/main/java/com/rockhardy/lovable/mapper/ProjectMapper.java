package com.rockhardy.lovable.mapper;

import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;
import com.rockhardy.lovable.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    @Mapping(source = "name",target = "projectName")
    ProjectSummaryResponse toProjectSummaryResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);
}
