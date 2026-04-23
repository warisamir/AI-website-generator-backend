package com.rockhardy.lovable.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
@Setter
public class ProjectFile {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "project_id",nullable = false)
        Project project;
        @Column(nullable = false)
        String path;

        String minioObjectKey;

        @CreationTimestamp
        Instant createdAt;
        @UpdateTimestamp
        Instant updatedAt;

}
