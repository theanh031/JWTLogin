package com.example.apitoregisterphone.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public interface IJWTService {
     String extractUsername(String token);
     <T> T extractClaim(String token, Function<Claims, T> claimResolver);
     String generateToken(UserDetails userDetails);
     String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
     );
    String generateRefreshToken(UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
}
