package com.rockhardy.lovable.entity;

import com.rockhardy.lovable.Enum.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "user_id")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "plan_id")
    Plan plan;
    @Enumerated(value = EnumType.STRING)
    SubscriptionStatus status;

    String stripeSubscriptionId;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd=false;
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;
}
