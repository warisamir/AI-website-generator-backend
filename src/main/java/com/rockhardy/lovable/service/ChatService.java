package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);

}
