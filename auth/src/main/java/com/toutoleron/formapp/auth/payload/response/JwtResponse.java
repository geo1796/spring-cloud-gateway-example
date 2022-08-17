package com.toutoleron.formapp.auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
    private String refreshToken;
    private String email;
    private List<String> roles;
}
