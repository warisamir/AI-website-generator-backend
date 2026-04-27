package com.rockhardy.lovable.repository;

import com.rockhardy.lovable.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectFileRepository extends JpaRepository<ProjectFile,Long> {

     List<ProjectFile> findByProjectId(Long projectId) ;

    Optional findByProjectIdAndPath(Long projectId, String cleanpath);
}
