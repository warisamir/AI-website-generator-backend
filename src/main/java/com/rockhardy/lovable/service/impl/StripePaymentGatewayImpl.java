package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;
import com.rockhardy.lovable.service.PaymentGatwayService;

public class StripePaymentGatewayImpl implements PaymentGatwayService {
    @Override
    public SubscriptionResponse getMySubscription() {
        return null;
    }

    @Override
    public CheckoutResponse createCheckout(CheckoutResponse request) {
        return null;
    }
}
