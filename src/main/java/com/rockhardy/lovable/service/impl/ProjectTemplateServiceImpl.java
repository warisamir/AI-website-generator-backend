package com.rockhardy.lovable.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rockhardy.lovable.entity.Project;
import com.rockhardy.lovable.entity.ProjectFile;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.repository.ProjectFileRepository;
import com.rockhardy.lovable.repository.ProjectRepository;
import com.rockhardy.lovable.service.ProjectTemplateService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProjectTemplateServiceImpl implements ProjectTemplateService {

    MinioClient minioClient;
    ProjectFileRepository projectFileRepository;
    ProjectRepository projectRepository;
    private static final String TEMPLATE_BUCKET="starter-projects";
    private static final String TARGET_BUCKET="projects";
    private static final String TEMPLATE_NAME="react-vite-tailwind-daisyui-starter";
    @Override
    public void initializeProjectFromTemaplate(Long projectId) {
        Project project=projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Project"+projectId.toString()+"not found"));
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(TARGET_BUCKET)
                            .prefix(TEMPLATE_NAME + "/")
                            .recursive(true)  // if put false it only give the 1 layer files not folder files
                            .build()
            );
            List<ProjectFile> filesToSave = new ArrayList<>();
            for (Result<Item> result : results) {
                Item item = result.get();
                String sourceKey = item.objectName();
                String cleanPath = sourceKey.replaceFirst(TEMPLATE_NAME + "/", "");
                String destKey = projectId + "/" + cleanPath;
                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .bucket(TARGET_BUCKET)
                                .object(destKey)
                                .source(
                                        CopySource.builder()
                                                .bucket(TEMPLATE_BUCKET)
                                                .object(sourceKey)
                                                .build()
                                )
                                .build()
                );
                ProjectFile pf= ProjectFile.builder()
                        .project(project)
                        .path(cleanPath)
                        .minioObjectKey(destKey)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();
                filesToSave.add(pf);
            }
            projectFileRepository.saveAll(filesToSave);
        }
        catch (Exception ex){
            throw new RuntimeException("Failed to initialize the project from template ",ex);
        }
    }
}
