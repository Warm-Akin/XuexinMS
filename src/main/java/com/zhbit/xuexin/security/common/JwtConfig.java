package com.zhbit.xuexin.security.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:config/jwt-setting.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "jwt", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class JwtConfig {

    private Integer tokenExpireTime;

    private Integer tokenRefreshExpireTime;

    private String tokenSigningKey;

    private Integer tokenCookieMaxAge;

    private Integer tokenRefreshCookieMaxAge;

    public Integer getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Integer tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Integer getTokenRefreshExpireTime() {
        return tokenRefreshExpireTime;
    }

    public void setTokenRefreshExpireTime(Integer tokenRefreshExpireTime) {
        this.tokenRefreshExpireTime = tokenRefreshExpireTime;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public Integer getTokenCookieMaxAge() {
        return tokenCookieMaxAge;
    }

    public void setTokenCookieMaxAge(Integer tokenCookieMaxAge) {
        this.tokenCookieMaxAge = tokenCookieMaxAge;
    }

    public Integer getTokenRefreshCookieMaxAge() {
        return tokenRefreshCookieMaxAge;
    }

    public void setTokenRefreshCookieMaxAge(Integer tokenRefreshCookieMaxAge) {
        this.tokenRefreshCookieMaxAge = tokenRefreshCookieMaxAge;
    }
}
