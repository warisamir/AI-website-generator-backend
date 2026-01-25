package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.auth.AuthResponse;
import com.rockhardy.lovable.dto.auth.LoginRequest;
import com.rockhardy.lovable.dto.auth.SignupRequest;
import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.exception.BadRequestException;
import com.rockhardy.lovable.mapper.UserMapper;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.security.AuthUtils;
import com.rockhardy.lovable.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtils authUtils;
    AuthenticationManager authenticationManager;
    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
       userRepository.findByUsername(signupRequest.username()).ifPresent((user)->{
           log.error("User already exist with username:{}",signupRequest.username());
            throw new BadRequestException("User already exist");
        });
       User user=userMapper.toEntity(signupRequest);
       user.setPassword(passwordEncoder.encode(signupRequest.password()));
       user=userRepository.save(user);
       String token= authUtils.generateAccessToken(user);
        return new AuthResponse(token,userMapper.toUserProfileRespoonse(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password())
        );
        User user =(User)authentication.getPrincipal();
        String token = authUtils.generateAccessToken(user);
        return new AuthResponse(token,userMapper.toUserProfileRespoonse(user));
    }
}
