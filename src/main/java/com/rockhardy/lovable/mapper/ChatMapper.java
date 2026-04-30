package com.rockhardy.lovable.mapper;

import com.rockhardy.lovable.dto.chat.ChatResponse;
import com.rockhardy.lovable.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);

}
