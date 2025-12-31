package com.rockhardy.lovable.security;

import com.rockhardy.lovable.entity.User;
import com.rockhardy.lovable.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Component
public class AuthUtils {
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;
    private final UserRepository userRepository;

    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getName())
                .claim("userId",user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }
    public JwtUserPrincipal verifyAccessToken(String token){
        Claims claims= (Claims) Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId= Long.parseLong(claims.get("userId",String.class));
        String username=claims.getSubject();
        return new JwtUserPrincipal(userId,username,new ArrayList<>());
    }
    public Long getCurrentUserId(){
        Authentication authenthencation= SecurityContextHolder.getContext().getAuthentication();
        if(authenthencation==null || !(authenthencation.getPrincipal() instanceof JwtUserPrincipal userPrincipal)){
            throw  new AuthenticationCredentialsNotFoundException("No Jwt found");
        }
        return userPrincipal.userId();
    }
}
