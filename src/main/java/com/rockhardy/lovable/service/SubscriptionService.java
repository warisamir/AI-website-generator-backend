package com.rockhardy.lovable.service;
import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    PortalResponse openCustomerPortal(Long userId) ;
    SubscriptionResponse getMySubscription(Long userId) ;
    CheckoutResponse createCheckout(CheckoutResponse request, Long userId);
}
