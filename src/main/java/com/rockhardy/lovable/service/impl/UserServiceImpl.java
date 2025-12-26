package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.auth.UserProfileResponse;
import com.rockhardy.lovable.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public UserProfileResponse getUser(Long userId) {
        return null;
    }
}
