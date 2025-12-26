package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.dto.subscription.CheckoutResponse;
import com.rockhardy.lovable.dto.subscription.PlanResponse;
import com.rockhardy.lovable.dto.subscription.PortalResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;
import com.rockhardy.lovable.service.PlanService;
import com.rockhardy.lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class BillingController {
    private final PlanService planService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/api/plans")
    public ResponseEntity<PlanResponse> getAllPlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId=1L;
         return ResponseEntity.ok(subscriptionService.getMySubscription(userId));
    }
    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(
            @RequestBody CheckoutResponse request
            ){
        Long userId=1L;
        return ResponseEntity.ok(subscriptionService.createCheckout(request,userId));
    }
    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse>openCustomerPortal(){
        Long userId=1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }
}
