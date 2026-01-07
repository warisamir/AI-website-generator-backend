package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.dto.project.ProjectRequest;
import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;
import com.rockhardy.lovable.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ProjectController {
    ProjectService projectService;
    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>>getProject(){
        return ResponseEntity.ok(projectService.getUserProjects());
    }
    @GetMapping("/{id}")
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectResponse getProjectById(@PathVariable Long id){
        Long userId =1L;
        return (projectService.getProjectById(id));
    }
    @PostMapping
    public ResponseEntity<ProjectResponse>createProject(@RequestBody @Valid ProjectRequest projectRequest){
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(projectService.createProject(projectRequest));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@security.canEditProject(#projectId)")
    public ResponseEntity<ProjectResponse>updateProject(@PathVariable Long projectId,@RequestBody @Valid  ProjectRequest request){
        return ResponseEntity.ok(projectService.updateProject(projectId,request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@security.canDeleteProject(#projectId")
    public ResponseEntity<Void>deleteProject(@PathVariable Long projectId){
        projectService.softDelete(projectId);
        return ResponseEntity.noContent().build();
    }
}
