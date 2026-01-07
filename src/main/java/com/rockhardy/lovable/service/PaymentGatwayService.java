package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;

public interface PaymentGatwayService {
    SubscriptionResponse getMySubscription() ;
    CheckoutResponse createCheckout(CheckoutResponse request );

}
