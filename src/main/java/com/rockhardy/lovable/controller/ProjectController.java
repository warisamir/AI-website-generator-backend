package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.dto.project.ProjectRequest;
import com.rockhardy.lovable.dto.project.ProjectResponse;
import com.rockhardy.lovable.dto.project.ProjectSummaryResponse;
import com.rockhardy.lovable.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Long userId=1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }
    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id){
        Long userId =1L;
        return (projectService.getProjectById(id,userId));
    }
    @PostMapping
    public ResponseEntity<ProjectResponse>createProject(@RequestBody ProjectRequest projectRequest){
        Long userId=1L;
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(projectService.createProject(projectRequest,userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse>updateProject(@PathVariable Long id,@RequestBody ProjectRequest request){
        Long userId =1L;
        return ResponseEntity.ok(projectService.updateProject(id,request,userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteProject(@PathVariable Long id){
        Long userId=1L;
        projectService.softDelete(id,userId);
        return ResponseEntity.noContent().build();
    }
}
