package com.example.apitoregisterphone.Controller;

import com.example.apitoregisterphone.common.Request.AuthenticationRequest;
import com.example.apitoregisterphone.common.Request.RegisterRequest;
import com.example.apitoregisterphone.common.Response.ResponseToken;
import com.example.apitoregisterphone.services.Impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class authenticationController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<ResponseToken> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<ResponseToken> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    
    @PostMapping("/refresh-token")
    public  void refreshToken(
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
