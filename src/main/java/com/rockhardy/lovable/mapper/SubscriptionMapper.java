package com.rockhardy.lovable.mapper;

import com.rockhardy.lovable.dto.subscription.PlanResponse;
import com.rockhardy.lovable.dto.subscription.SubscriptionResponse;
import com.rockhardy.lovable.entity.Plan;
import com.rockhardy.lovable.entity.Subscription;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);
    PlanResponse toPlanResponse(Plan plan);
}
