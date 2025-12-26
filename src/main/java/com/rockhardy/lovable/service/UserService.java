package com.rockhardy.lovable.service;

import com.rockhardy.lovable.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getUser(Long userId);
}
