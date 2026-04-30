package com.rockhardy.lovable.dto.chat;

import com.rockhardy.lovable.Enum.ChatEventType;
import com.rockhardy.lovable.Enum.MessageRole;
import com.rockhardy.lovable.entity.ChatEvent;
import com.rockhardy.lovable.entity.ChatMessage;
import com.rockhardy.lovable.entity.ChatSession;

import java.time.Instant;
import java.util.List;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filepath ,
        String metadata
) {
}
