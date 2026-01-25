package com.rockhardy.lovable.mapper;

import com.rockhardy.lovable.dto.auth.SignupRequest;
import com.rockhardy.lovable.dto.auth.UserProfileResponse;
import com.rockhardy.lovable.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public  interface UserMapper {
    User toEntity(SignupRequest signupRequest);
    UserProfileResponse toUserProfileRespoonse(User user);
}
