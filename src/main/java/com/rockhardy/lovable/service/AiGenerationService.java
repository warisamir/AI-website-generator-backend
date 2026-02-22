package com.rockhardy.lovable.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<String> streamResponse(String message , Long projectId);
}
