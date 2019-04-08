package com.zhbit.xuexin.security.common;

public class AccessToken {
    private String rawToken;

    public AccessToken(String rawToken) {
        this.rawToken = rawToken;
    }

    public String getRawToken() {
        return rawToken;
    }

    public void setRawToken(String rawToken) {
        this.rawToken = rawToken;
    }
}
