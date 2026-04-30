package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.chat.ChatResponse;
import com.rockhardy.lovable.entity.ChatMessage;
import com.rockhardy.lovable.entity.ChatSession;
import com.rockhardy.lovable.entity.ChatSessionId;
import com.rockhardy.lovable.mapper.ChatMapper;
import com.rockhardy.lovable.repository.ChatMessageRepository;
import com.rockhardy.lovable.repository.ChatSessionRepository;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtils authUtils;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId= authUtils.getCurrentUserId();
        ChatSession chatSession=chatSessionRepository.getReferenceById(new ChatSessionId(projectId,userId));
        List<ChatMessage>chatMessageList=chatMessageRepository.findByChatSession(chatSession);

        return chatMapper.fromListOfChatMessage(chatMessageList);
    }
}
