package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.dto.chat.ChatRequest;
import com.rockhardy.lovable.service.AiGenerationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatController {
    AiGenerationService aiGenerationService;

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(
            @RequestBody ChatRequest request) {
        return aiGenerationService.streamResponse(request.message(),request.projectId())
                .map(data->ServerSentEvent.<String>builder()
                        .data(data)
                        .build());
    }
}
