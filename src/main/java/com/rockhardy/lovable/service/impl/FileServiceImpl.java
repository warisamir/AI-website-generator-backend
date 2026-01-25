package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.file.FileContentResponse;
import com.rockhardy.lovable.dto.file.FileNode;
import com.rockhardy.lovable.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileNode> getfileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long userId, String path) {
        return null;
    }
}
