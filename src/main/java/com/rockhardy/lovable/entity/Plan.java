package com.rockhardy.lovable.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {
     Long id;
     String name;
     String stripePriceId;
     Integer maxProjects;
     Integer maxTokenPerDay;
     Integer maxPreviews;
     Boolean unlimitedAi;
     Boolean active;
}
