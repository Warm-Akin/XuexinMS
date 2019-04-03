package com.zhbit.xuexin.security.common;

import io.jsonwebtoken.Claims;

import java.util.Map;

public class JwtToken {

    private String rawToken;

    private Map<String, Object> claims;

    public JwtToken(String rawToken, Map<String, Object> claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public String getRawToken() {
        return rawToken;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }
}
