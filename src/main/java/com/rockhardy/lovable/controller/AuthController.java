package com.rockhardy.lovable.controller;

import com.rockhardy.lovable.dto.auth.AuthResponse;
import com.rockhardy.lovable.dto.auth.LoginRequest;
import com.rockhardy.lovable.dto.auth.SignupRequest;
import com.rockhardy.lovable.dto.auth.UserProfileResponse;
import com.rockhardy.lovable.service.AuthService;
import com.rockhardy.lovable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private AuthService authService;
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>signup(SignupRequest signupRequest){
        return ResponseEntity.ok(authService.signup(signupRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse>getProfile(){
        Long userId=1L;
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
