package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.file.FileContentResponse;
import com.rockhardy.lovable.dto.file.FileNode;
import com.rockhardy.lovable.service.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectFileServiceImpl implements ProjectFileService {
    @Override
    public List<FileNode> getfileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long userId, String path) {
        return null;
    }

    @Override
    public void saveFiile(Long projectId, String filepath, String fileContent) {
        log.info("moving into saving file");
        //save the meta data in postgres
        // save the file in minio
    }
}
