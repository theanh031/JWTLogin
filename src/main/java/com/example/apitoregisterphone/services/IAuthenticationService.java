package com.example.apitoregisterphone.services;

import com.example.apitoregisterphone.common.Request.AuthenticationRequest;
import com.example.apitoregisterphone.common.Request.RegisterRequest;
import com.example.apitoregisterphone.common.Response.ResponseToken;
import com.example.apitoregisterphone.models.users.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;


public interface IAuthenticationService {
    ResponseToken register(RegisterRequest request);
    ResponseToken authenticate(AuthenticationRequest request);
    void revokeAllUserTokens(User user);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
