package com.spring.tradex.Controller;

import com.spring.tradex.DTO.Auth.AuthResponse;
import com.spring.tradex.DTO.Auth.LoginRequest;
import com.spring.tradex.DTO.Auth.RegisterRequest;
import com.spring.tradex.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return new AuthResponse("User registered successfully");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        String token = authService.authenticate(request.getEmail(), request.getPassword());

        return new AuthResponse(token);
    }
}