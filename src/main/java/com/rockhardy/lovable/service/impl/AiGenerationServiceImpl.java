package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.Enum.ChatEventType;
import com.rockhardy.lovable.Enum.MessageRole;
import com.rockhardy.lovable.entity.*;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.llm.LlmResponseParser;
import com.rockhardy.lovable.llm.PromptUtils;
import com.rockhardy.lovable.llm.advisors.FileTreeContextAdvisor;
import com.rockhardy.lovable.llm.tools.CodeGenerationTools;
import com.rockhardy.lovable.repository.ChatMessageRepository;
import com.rockhardy.lovable.repository.ChatSessionRepository;
import com.rockhardy.lovable.repository.ProjectRepository;
import com.rockhardy.lovable.repository.UserRepository;
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

import java.util.List;
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
    FileTreeContextAdvisor fileTreeContextAdvisor;
    LlmResponseParser llmResponseParser;
    ProjectRepository projectRepository;
    ChatSessionRepository chatSessionRepository;
    UserRepository userRepository;
    ChatMessageRepository chatMessageRepository;
    Pattern FILE_TAG_PATTERN=Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);
    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String>streamResponse(String userMessage, Long projectId){
        Long userId=authUtils.getCurrentUserId();
        ChatSession chatSession=createChatSessionIfNotExists(projectId,userId);
        Map<String, Object>advisorParams=Map.of("userId",userId,"projectId",projectId);
        StringBuilder fullResponseBuffer= new StringBuilder();
        CodeGenerationTools codeGenerationTools= new CodeGenerationTools(projectFileService,projectId);
         return  chatClient.prompt().
                system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                 .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                    advisorSpec.advisors(fileTreeContextAdvisor);
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
//                        parseAndSaveFile(fullResponseBuffer.toString(),projectId);
                        finalizeChat(userMessage,chatSession,fullResponseBuffer.toString(),projectId);
                    });
                })
                .doOnError(error->{
                    log.error("Error During Streaming for projectid: {}",projectId);
                })
//                .map(response-> Objects.requireNonNull(response.getResult().getOutput().getText()));
                 .handle((resp, sink) -> {
                     var result = resp != null ? resp.getResult() : null;
                     var output = result != null ? result.getOutput() : null;
                     var text   = output != null ? output.getText() : null;

                     if (text != null && !text.isEmpty()) {
                         sink.next(text);
                     }
                     // else: ignore non-text events
                 });
    }

    private void finalizeChat(String userMessage, ChatSession chatSession,String fulltext,Long projectId){
        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatSession(chatSession)
                        .role(MessageRole.USER)
                        .content(userMessage)
                        .build()
        );
        ChatMessage assistantChatMessage= ChatMessage.builder()
                .chatSession(chatSession)
                .role(MessageRole.ASSISTANT)
                .build();
        List<ChatEvent> chatEventList= llmResponseParser.parseChatEvents(fulltext,assistantChatMessage);
        chatEventList.stream()
                .filter(e->e.getType()== ChatEventType.FILE_EDIT)
                .forEach(e->projectFileService.saveFiile(projectId,e.getFilePath(),e.getContent()));

    }
    public ChatSession createChatSessionIfNotExists(Long projectId,Long userId){
       ChatSessionId chatSessionId=new ChatSessionId(projectId,userId);
       ChatSession chatSession=chatSessionRepository.findById(chatSessionId).orElse(null);
       if(chatSession==null){
           Project project=projectRepository.findById(projectId).orElseThrow(
                   ()->new ResourceNotFoundException("project"+projectId.toString()));
           User user=userRepository.findById(userId).orElseThrow(
                   ()->new ResourceNotFoundException("User "+userId.toString()));
           chatSession = ChatSession.builder()
                   .id(chatSessionId)
                   .project(project)
                   .user(user)
                   .build();
           chatSession = chatSessionRepository.save(chatSession);
       }
       return chatSession;
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
