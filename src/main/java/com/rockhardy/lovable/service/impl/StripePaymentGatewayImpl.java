package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.subscription.CheckoutRequest;
import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.entity.Plan;
import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.repository.PlanRepository;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.PaymentGatwayService;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentGatewayImpl implements PaymentGatwayService {
    private final AuthUtils authUtils;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    @Value("${client-url}")
    private String domain;
    @Override
    public PortalResponse openCustomerPortal() {
        Long userIda=authUtils.getCurrentUserId();
        return null;
    }

    @Override
    public CheckoutResponse createCheckout(CheckoutRequest request) {
        try {
        Long userId=authUtils.getCurrentUserId();
        Plan plan= planRepository.findById(request.planId())
                .orElseThrow(()->new ResourceNotFoundException("Plan "+request.planId().toString()));
            User user=userRepository.findById(userId).orElseThrow(()->
                    new ResourceNotFoundException("user not found with this ID"+userId));

            var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        new SessionCreateParams.
                                SubscriptionData.Builder()
                                .setBillingMode(SessionCreateParams.SubscriptionData.BillingMode.builder()
                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                .build())
                                .build())
                .setSuccessUrl(domain + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(domain+"/cancel.html")
                .putMetadata("user_id",userId.toString())
                .putMetadata("plan_id",plan.getId().toString());
            String stripeCustomerId=user.getStripeCustomerId();
            if(stripeCustomerId==null ||stripeCustomerId.isEmpty()){
                params.setCustomerEmail(user.getUsername());
            }else{
                params.setCustomer(stripeCustomerId);
            }
        Session session = Session.create(params.build());
        return  new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
        log.info("type");
    }
}
