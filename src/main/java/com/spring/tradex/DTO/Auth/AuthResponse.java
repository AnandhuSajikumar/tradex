package com.spring.tradex.DTO.Auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
