package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.file.FileContentResponse;
import com.rockhardy.lovable.dto.file.FileNode;
import com.rockhardy.lovable.entity.Project;
import com.rockhardy.lovable.entity.ProjectFile;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.mapper.ProjectFileMapper;
import com.rockhardy.lovable.repository.ProjectFileRepository;
import com.rockhardy.lovable.repository.ProjectRepository;
import com.rockhardy.lovable.service.ProjectFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {
    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final MinioClient minioClient;
    private final ProjectFileMapper projectFileMapper;
    @Value("${minio.project-bucket}")
    private String projectBucket;

    private static final String BUCKET_NAME="projects";
    @Override
    public List<FileNode> getfileTree(Long projectId) {
        List<ProjectFile>fileList=projectFileRepository.findByProjectId(projectId);
        return projectFileMapper.toListOfFileNode(fileList);
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path) {
        String objectName=projectId+"/"+path;
        try(
                InputStream is=minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(objectName)
                                .build())){
            String content= new String(is.readAllBytes(),StandardCharsets.UTF_8);
            return new FileContentResponse(path,content);
        } catch (Exception e) {
            log.error("failed to read file {}/{}", projectId, path,e);
            throw new RuntimeException("failed to read file content",e);
        }
    }

    @Override
    public void saveFiile(Long projectId, String filepath, String fileContent) {
        log.info("moving into saving file");
        Project project= projectRepository.findById(projectId).orElseThrow(()->
            new ResourceNotFoundException("Project "+ projectId.toString()));

        String cleanpath= filepath.startsWith("/")?filepath.substring(1):filepath;
        String objectKey= projectId+"/"+cleanpath;

        try{
            byte[] contentBytes=fileContent.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream=new ByteArrayInputStream(contentBytes);
            //saivng the fileocntent
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(projectBucket)
                    .object(objectKey)
                    .stream(inputStream,contentBytes.length,-1)
                            .contentType(determineContentType(filepath))
                    .build());
            ProjectFile file = (ProjectFile) projectFileRepository.findByProjectIdAndPath(projectId, cleanpath)
                    .orElseGet(() -> ProjectFile.builder()
                            .project(project)
                            .path(cleanpath)
                            .minioObjectKey(objectKey) // Use the key we generated
                            .createdAt(Instant.now())
                            .build());

            file.setUpdatedAt(Instant.now());
                    projectFileRepository.save(file);
                    log.info("file save sucessfully {}",objectKey);
        }
        catch (Exception e){
            throw new RuntimeException("file saved failed",e);
        }
    }
    private String determineContentType(String path){
        String type= URLConnection.guessContentTypeFromName(path);
        if(type!=null) return type;
        if(path.endsWith(".jsx")|| path.endsWith(".ts")|| path.endsWith(".tsx"))return "text/javascript";
        if(path.endsWith(".json")) return "application/json";
        if(path.endsWith(".css")) return  "text/css";
        return "text/plain";
    }
}
