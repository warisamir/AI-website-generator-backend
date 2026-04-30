package com.rockhardy.lovable.entity;

import com.rockhardy.lovable.Enum.MessageRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumns({
    @JoinColumn(name = "project_id",referencedColumnName = "project_id",nullable = false),
     @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)})
    ChatSession chatSession;

    @OneToMany(mappedBy = "chatMessage",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @OrderBy("sequenceOrder ASC")
    List<ChatEvent>events;

    @Column(columnDefinition = "text", nullable = false)
    String content;
    String toolCalls;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageRole role;

    Instant tokenUsed;
    Instant createdAt;
    Instant updatedAt;

}
