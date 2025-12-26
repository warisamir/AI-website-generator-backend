package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.auth.AuthResponse;
import com.rockhardy.lovable.dto.auth.LoginRequest;
import com.rockhardy.lovable.dto.auth.SignupRequest;
import com.rockhardy.lovable.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        return null;
    }
}
