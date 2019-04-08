package com.zhbit.xuexin.security.common;

import io.jsonwebtoken.Claims;

import java.util.Map;

public class JwtToken {

    private String rawToken;

    private Claims claims;

    public JwtToken() {

    }

    public JwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public JwtToken(String rawToken) {
        this.rawToken = rawToken;
    }

    public String getRawToken() {
        return rawToken;
    }

    public Claims getClaims() {
        return claims;
    }
}
