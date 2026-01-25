package com.rockhardy.lovable.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfiguration {
    @Value("${stripe.api.secret}")
    private String secretkey;
    @PostConstruct
    public void init()
    {
        Stripe.apiKey=secretkey;
    }
}
