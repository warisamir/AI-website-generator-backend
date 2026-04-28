package com.rockhardy.lovable.llm.advisors;

import com.rockhardy.lovable.dto.file.FileNode;
import com.rockhardy.lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileTreeContextAdvisor implements StreamAdvisor {

    private final ProjectFileService projectFileService;
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Map<String,Object> context=chatClientRequest.context();
        Long userId= Long.parseLong(context.getOrDefault("userId",0).toString());
        Long projectId=Long.parseLong(context.getOrDefault("projectId",0 ).toString());
        ChatClientRequest argumentedRequestWithFileTree = argumentRequestWithFileTree(chatClientRequest,projectId,userId);
        return streamAdvisorChain.nextStream(argumentedRequestWithFileTree);
    }

    private ChatClientRequest argumentRequestWithFileTree(ChatClientRequest request,Long projectId,Long userId){
        List<Message> incomingMessages=request.prompt().getInstructions();
        Message systemMessages=incomingMessages.stream()
                .filter(m->m.getMessageType()== MessageType.SYSTEM)
                .findFirst().orElse(null);
        List<Message> userMessage=incomingMessages.stream().filter(
                m->m.getMessageType()!=MessageType.SYSTEM)
                .toList();
        List<Message>allMessages= new ArrayList<>();
        if(systemMessages!=null){
            allMessages.add(systemMessages);
        }

        List<FileNode>fileTree = projectFileService.getfileTree(projectId,userId);
        String fileTreeContext= "\n\n  --------FILE TREE -------- \n\n"+fileTree.toString();
        allMessages.add(new SystemMessage(fileTreeContext));
        allMessages.addAll(userMessage);
        return request
                .mutate()
                .prompt(new Prompt(allMessages,request.prompt().getOptions()))
                .build();
    }
    @Override
    public String getName() {
        return "FileTreeContextAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
