package com.rockhardy.lovable.service;
import com.rockhardy.lovable.Enum.SubscriptionStatus;
import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;

import java.time.Instant;

public interface SubscriptionService {
     SubscriptionResponse getCurrentSubscription() ;

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);
}
