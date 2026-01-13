package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.Enum.SubscriptionStatus;
import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;
import com.rockhardy.lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription() {
        return null;
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {

    }

    @Override
    public void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }
}
