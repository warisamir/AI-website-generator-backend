package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.file.FileContentResponse;
import com.rockhardy.lovable.dto.file.FileNode;

import java.util.List;

public interface FileService {

     List<FileNode> getfileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long userId, String path);
}
