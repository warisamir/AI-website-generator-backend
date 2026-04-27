package com.rockhardy.lovable.mapper;

import com.rockhardy.lovable.dto.file.FileNode;
import com.rockhardy.lovable.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {
    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
