package com.example.apitoregisterphone.services.Impl;

import com.example.apitoregisterphone.common.Request.AuthenticationRequest;
import com.example.apitoregisterphone.common.Request.RegisterRequest;
import com.example.apitoregisterphone.common.Response.ResponseToken;
import com.example.apitoregisterphone.models.tokens.Token;
import com.example.apitoregisterphone.models.users.User;
import com.example.apitoregisterphone.repositories.tokenRepo;
import com.example.apitoregisterphone.repositories.userRepo;
import com.example.apitoregisterphone.services.IAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final userRepo _userRepo;
    private final tokenRepo _tokenRepo;
    private final PasswordEncoder _passwordEncoder;
    private final JWTService _jwtService;
    private final AuthenticationManager _authenticationManager;
    
    
    @Override
    public ResponseToken register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(_passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var saveUser = _userRepo.save(user);
        var token = _jwtService.generateToken(user);
        var refreshToken = _jwtService.generateRefreshToken(user);
        SaveUserToken(saveUser,token);
        return ResponseToken.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
    
    private void SaveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        _tokenRepo.save(token);
    }

    @Override
    public ResponseToken authenticate(AuthenticationRequest request) {
        _authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = _userRepo.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = _jwtService.generateToken(user);
        var refreshToken = _jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        SaveUserToken(user, jwtToken);
        
        return ResponseToken.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserToken = _tokenRepo.findAllValidTokenByUser(user.getId());
        if(validUserToken.isEmpty()) {
            return;
        }
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        _tokenRepo.saveAll(validUserToken);

    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = _jwtService.extractUsername(refreshToken);
        if(username != null) {
            var user = _userRepo.findByUsername(username).orElseThrow();
            if (_jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = _jwtService.generateToken(user);
                revokeAllUserTokens(user);
                SaveUserToken(user, accessToken);
                var authResponse = ResponseToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
        
                

    }
}
