package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.entity.ProjectMember;
import com.rockhardy.lovable.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<ProjectMember>>getProjectMembers(@PathVariable Long projectId){
        Long userId=1L;
        return ResponseEntity.ok(ProjectMemberService.getProjectMembers(projectId,userId));
    }
}
