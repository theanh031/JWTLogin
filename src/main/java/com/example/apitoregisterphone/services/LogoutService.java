package com.example.apitoregisterphone.services;

import com.example.apitoregisterphone.repositories.tokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final tokenRepo _tokenRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authheader = request.getHeader("Authorization");
        final String jwt;
        if(authheader == null || !authheader.startsWith("Bearer ")) {
            return;
        }
        jwt = authheader.substring(7);
        var storeToken = _tokenRepo.findByToken(jwt).orElse(null);
        if(storeToken == null) {
            storeToken.setExpired(true);
            storeToken.setRevoked(true);
            _tokenRepo.save(storeToken);
            SecurityContextHolder.clearContext();
        }
    }
}
