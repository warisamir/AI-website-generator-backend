package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.dto.file.FileContentResponse;
import com.rockhardy.lovable.dto.file.FileNode;
import com.rockhardy.lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/project/{projectId}/files")
public class FileController {
    private final ProjectFileService projectFileService;

    @GetMapping
    public ResponseEntity<List<FileNode>>getFileTree(@PathVariable Long projectId){
        Long userId=1L;
        return ResponseEntity.ok(projectFileService.getfileTree(projectId,userId));
    }
    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse>getFileContent(@PathVariable String path){
        Long userId=1L;
        return ResponseEntity.ok(projectFileService.getFileContent(userId,path));
    }
}
