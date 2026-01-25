package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.auth.AuthResponse;
import com.rockhardy.lovable.dto.auth.LoginRequest;
import com.rockhardy.lovable.dto.auth.SignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {
     AuthResponse signup(SignupRequest signupRequest);

    AuthResponse login(LoginRequest loginRequest);
}
