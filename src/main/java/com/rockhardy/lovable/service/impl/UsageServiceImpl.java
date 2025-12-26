package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.subscription.PlanLimitResponse;
import com.rockhardy.lovable.dto.subscription.UsageTodayResponse;
import com.rockhardy.lovable.service.UsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
