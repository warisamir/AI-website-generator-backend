package com.rockhardy.lovable.dto.chat;

import com.rockhardy.lovable.Enum.MessageRole;
import com.rockhardy.lovable.entity.ChatEvent;
import com.rockhardy.lovable.entity.ChatSession;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        ChatSession chatSession,
        MessageRole messageRole,
        List<ChatEvent>events,
        String content,
        Integer tokensUsed,
        Instant createdAt
) {
}
