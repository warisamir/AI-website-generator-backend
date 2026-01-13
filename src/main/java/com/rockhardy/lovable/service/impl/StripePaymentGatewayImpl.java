package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.Enum.SubscriptionStatus;
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
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentGatewayImpl implements PaymentGatwayService {
    private final AuthUtils authUtils;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionServiceImpl subscriptionService;
    @Value("${client-url}")
    private String domain;
    @Override
    public PortalResponse openCustomerPortal() {
        Long userId=authUtils.getCurrentUserId();
        return null;
    }

    @Override
    public CheckoutResponse createCheckout(CheckoutRequest request) {
        try {
        Long userId=authUtils.getCurrentUserId();
        Plan plan= planRepository.findById(request.planId())
                .orElseThrow(()->new ResourceNotFoundException("Plan "+request.planId().toString()));
            User user=getUser(userId);
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
        log.info("handling the type of event {}",type);
        switch(type){
            case "checkout.session.completed" -> handeCheckoutSessionCompleted((Session) stripeObject,metadata );
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
            case "customer.subscription.deleted"-> handleCustomerSubscriptionDeleted((Subscription) stripeObject);
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject);
            case "invoice.payment_failed" ->handleInvoicePaymentFailed((Invoice) stripeObject);
            default ->log.debug("ignoring event type :{}",type);
        }
    }

    private void handleInvoicePaymentFailed(Invoice invoice) {
    }

    private void handleInvoicePaid(Invoice invoice) {
    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription) {
    }

    private void handeCheckoutSessionCompleted(Session session,Map<String, String> metadata) {
        if(session==null){
            log.error("Session object was null");
        }
        Long userId= Long.parseLong(metadata.get("user_id"));
        Long planId= Long.parseLong(metadata.get("plan_id"));
        String subscriptionId= session.getSubscription();
        String customerId= session.getCustomer();
        User user = getUser(userId);
        if(user.getStripeCustomerId()==null){
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }
        subscriptionService.activateSubscription(userId,planId,subscriptionId,customerId);
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription) {
        if(subscription==null){
            log.error("Subscription object was null");
            return ;
        }
        SubscriptionStatus status=mapStripeStatusToEnum(subscription.getStatus());
        if(status==null){
            log.warn("UNknown status {} for subscription {} ",subscription.getStatus(),subscription.getId());
        }
        SubscriptionItem item=subscription.getItems().getData().get(0);
        Instant periodStart= toInstant(item.getCurrentPeriodStart());
        Instant periodEnd= toInstant(item.getCurrentPeriodEnd());
        Long planId=resolvePlanId(item.getPrice());
        subscriptionService.updateSubscription(
                subscription.getId(),status,periodStart,
                periodEnd,subscription.getCancelAtPeriodEnd(),planId
        );
    }

    private Long resolvePlanId(Price price) {
        if(price==null ||price.getId()==null)
            return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private Instant toInstant(Long currentPeriodStart) {
        return currentPeriodStart!=null?Instant.ofEpochMilli(currentPeriodStart);
    }

    private User getUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user not found with this ID"+userId));
        return user;
    }
    private SubscriptionStatus mapStripeStatusToEnum(String status){
        return switch (status){
            case "active"-> SubscriptionStatus.ACTIVE;
            case "trailing"-> SubscriptionStatus.TRAILING;
            case "past_due" -> SubscriptionStatus.PAST_DUE;
            case "cancelled"-> SubscriptionStatus.CANCELLED;
            case "incomplete"-> SubscriptionStatus.INCOMPLETED;
            default -> {
                log.debug("Unnamed Stripe status: {}",status);
                yield null;
            }
        };
    }
}
