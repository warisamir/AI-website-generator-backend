package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.Enum.SubscriptionStatus;
import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PlanResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;
import com.rockhardy.lovable.entity.Plan;
import com.rockhardy.lovable.entity.Subscription;
import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.mapper.SubscriptionMapper;
import com.rockhardy.lovable.repository.PlanRepository;
import com.rockhardy.lovable.repository.SubscriptionRepository;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@Slf4j
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    AuthUtils authUtils;
    SubscriptionRepository subscriptionRepository;
    SubscriptionMapper subscriptionMapper;
    UserRepository userRepository;
    PlanRepository planRepository;
    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtils.getCurrentUserId();
        var currentSubscription=subscriptionRepository.findByUserIdAndStatusIn(userId,
                Set.of(
                SubscriptionStatus.ACTIVE,SubscriptionStatus.TRAILING,
                SubscriptionStatus.PAST_DUE
        )).orElse(new Subscription()
        );
        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        boolean exists= subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        Plan plan= getPlan(planId);
        User user= getUser(userId);
        Subscription subscription= Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETED)
                .build();
        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptionID, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription=ExtractSubscription(gatewaySubscriptionID);
        boolean hasSubscriptionUpdated=false;
        if(status!=null && status!=subscription.getStatus()){
            subscription.setStatus(status);
            hasSubscriptionUpdated=true;
        }
        if(periodStart!=null && periodStart.equals(subscription.getCurrentPeriodStart())){
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionUpdated=true;
        }
        if(periodEnd!=null && periodEnd.equals(subscription.getCurrentPeriodEnd())){
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionUpdated=true;
        }
        if(cancelAtPeriodEnd!=null && cancelAtPeriodEnd!=(subscription.getCancelAtPeriodEnd())){
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionUpdated=true;
        }
        if(planId!=null && !planId.equals(subscription.getPlan().getId())){
            subscription.setPlan(getPlan(planId));
            hasSubscriptionUpdated=true;
        }
        if(hasSubscriptionUpdated){
            log.debug("Subscription has been updated {}", gatewaySubscriptionID);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {
        Subscription subscription=ExtractSubscription(gatewaySubscriptionId);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);

    }

    @Override
    public void renewSubscription(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
        Subscription subscription= ExtractSubscription(gatewaySubscriptionId);
        Instant newStart =periodStart!=null? periodStart:subscription.getCurrentPeriodStart();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);
        if(subscription.getStatus()==SubscriptionStatus.PAST_DUE){
             subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionDue(String subId) {
        Subscription subscription=ExtractSubscription(subId);
        if(subscription.getStatus()==SubscriptionStatus.PAST_DUE){
            log.debug("subscription is past due {}",subId);
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
    }
    private User getUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user not found with this ID"+userId));
        return user;
    }
    private Plan getPlan(Long planId){
        Plan plan=planRepository.findById(planId).orElseThrow(()->
                new ResourceNotFoundException("user not found with this ID"+planId));
        return plan;
    }
    private Subscription ExtractSubscription(String gatewaySubscriptionId){
        Subscription subscription=subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId)
                .orElseThrow(()->new ResourceNotFoundException("Subscription"+gatewaySubscriptionId));
        return subscription;
    }
}

