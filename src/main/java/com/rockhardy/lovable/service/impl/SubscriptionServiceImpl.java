package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;
import com.rockhardy.lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }

    @Override
    public SubscriptionResponse getMySubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckout(CheckoutResponse request, Long userId) {
        return null;
    }
}
