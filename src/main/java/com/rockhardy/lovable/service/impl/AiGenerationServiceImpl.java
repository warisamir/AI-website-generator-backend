package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.llm.PromptUtils;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.AiGenerationService;
import com.rockhardy.lovable.service.ProjectFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {
    ChatClient chatClient;
    AuthUtils authUtils;
    ProjectFileService projectFileService;
    Pattern FILE_TAG_PATTERN=Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);
    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String>streamResponse(String userMessage, Long projectId){
        Long userId=authUtils.getCurrentUserId();
        createChatSessionIfNotExists(projectId,userId);
        Map<String, Object>advisorParams=Map.of("userId",userId,"projectId",projectId);
        StringBuilder fullResponseBuffer= new StringBuilder();
         return  chatClient.prompt().
                system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorParams);
                })
                .stream()
                .chatResponse()
                .doOnNext(response->{
                    String content=response.getResult().getOutput().getText();
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(()->{
                    Schedulers.boundedElastic().schedule(()->{
                        parseAndSaveFile(fullResponseBuffer.toString(),projectId);
                    });
                })
                .doOnError(error->{
                    log.error("Error During Streaming for projectid: {}",projectId);
                })
                .map(response-> Objects.requireNonNull(response.getResult().getOutput().getText()));
    }


    public void createChatSessionIfNotExists(Long projectId,Long userId){
    }


    public void parseAndSaveFile(String fullResponse,Long projectId){
        Matcher matcher=FILE_TAG_PATTERN.matcher(fullResponse);
        while (matcher.find()){
            String filepath=matcher.group(1);
            String fileContent=matcher.group(2).trim();
                projectFileService.saveFiile(projectId,filepath,fileContent);
        }
//        String dummy= """
//                <message>Im going to read the file and generate the code..</messsage>
//                <file path="src/App.jsx">
//                import App from '/App.jsx';
//                ....
//                </file>
//                """;

    }
}
