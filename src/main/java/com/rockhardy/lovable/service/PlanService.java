package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.subscription.PlanResponse;
import org.jspecify.annotations.Nullable;

public interface PlanService {

    PlanResponse getAllActivePlans();
}
