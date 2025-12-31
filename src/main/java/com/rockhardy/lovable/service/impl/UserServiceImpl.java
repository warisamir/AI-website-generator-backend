package com.rockhardy.lovable.service.impl;

import com.rockhardy.lovable.dto.auth.UserProfileResponse;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import com.rockhardy.lovable.repository.UserRepository;
import com.rockhardy.lovable.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService, UserDetailsService {
    UserRepository userRepository;
    @Override
    public UserProfileResponse getUser(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     return  userRepository.findByUsername(username).orElseThrow(()->{
         return new ResourceNotFoundException("resource name"+username+"not found");
     });
    }
}
