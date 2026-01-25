package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.subscription.PlanLimitResponse;
import com.rockhardy.lovable.dto.subscription.UsageTodayResponse;


public interface UsageService {
     UsageTodayResponse getTodayUsageOfUser(Long userId);
     PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
