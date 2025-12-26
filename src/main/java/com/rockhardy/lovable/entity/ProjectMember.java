package com.rockhardy.lovable.entity;

import com.rockhardy.lovable.Enum.ProjectRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "project_members")
public class ProjectMember {
    @EmbeddedId
    ProjectMemberId id;
    @ManyToOne
            @MapsId("projectId")
    Project project;
    @ManyToOne
            @MapsId("userId")
    User user;
    @Enumerated(EnumType.STRING)
            @Column(nullable = false)
    ProjectRole projectRole;
    Instant invitedAt;
    Instant acceptedAt;
}
