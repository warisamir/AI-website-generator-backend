package com.rockhardy.lovable.repository;

import com.rockhardy.lovable.entity.ChatSession;
import com.rockhardy.lovable.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession,ChatSessionId> {
}
