package com.rockhardy.lovable.entity;

import com.rockhardy.lovable.Enum.MessageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    Long id;
    ChatSession chatSession;
    String content;
    String toolCalls;
    MessageRole role;
    Instant tokenUsed;
    Instant createdAt;
    Instant updatedAt;

}
