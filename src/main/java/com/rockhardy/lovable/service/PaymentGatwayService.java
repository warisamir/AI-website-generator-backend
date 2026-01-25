package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.subscription.CheckoutRequest;
import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentGatwayService {
    PortalResponse openCustomerPortal();
    CheckoutResponse createCheckout(CheckoutRequest request );

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
