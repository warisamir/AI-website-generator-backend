package com.rockhardy.lovable.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Plan {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
             private Long Id;
     String name;
     @Column(unique = true)
     String stripePriceId;

     Integer maxProjects;
     Integer maxTokenPerDay;
     Integer maxPreviews;
     Boolean unlimitedAi;
     Boolean active;
}
