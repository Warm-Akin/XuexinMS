package com.zhbit.xuexin.security.util;

import com.zhbit.xuexin.security.common.JwtConfig;
import com.zhbit.xuexin.security.common.JwtToken;
import com.zhbit.xuexin.security.common.UserContext;
import com.zhbit.xuexin.security.constant.AuthenticationConstant;
import com.zhbit.xuexin.security.exception.AuthenticationTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Autowired
    JwtConfig jwtConfig;


    public JwtToken createAccessToken(UserContext userContext) {
        if (StringUtils.isEmpty(userContext.getUsername()))
            throw new IllegalArgumentException("找不到没有用户名,生成Token失败");
        if (null == userContext.getAuthorities() || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("用户没有任何权限");


        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
        claims.put("userId", userContext.getUser().getUserId());

//        Map<String, Object> claims = new HashMap<>();
//        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
//        claims.put("userInfo", userContext.getUser());
        claims.put("sub", userContext.getUsername());
        claims.put("created", new Date());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS512");
        headerMap.put("typ", "JWT");

        LocalDateTime nowTime = LocalDateTime.now();

        String token = Jwts.builder().setClaims(claims)
                .setHeader(headerMap)
                .setIssuer("xuexin.service")
                .setIssuedAt(Date.from(nowTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(nowTime.plusMinutes(jwtConfig.getTokenExpireTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getTokenSigningKey())
                .compact();
        return new JwtToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isEmpty(userContext.getUsername())) {
            throw new IllegalArgumentException("找不到没有用户名,生成Token失败");
        }

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", Arrays.asList(AuthenticationConstant.ROLE_REFRESH));

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("xuexin.service")
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(jwtConfig.getTokenRefreshExpireTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getTokenSigningKey())
                .compact();
        return new JwtToken(token, claims);
    }

    public Jws<Claims> parseClaims(String signingKey, String accessToken) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(accessToken);
        } catch (ExpiredJwtException e) {
            String tokenExpiredExceptionMsg = "Authentication Failed: JWT token expired";
            throw new AuthenticationTokenException(tokenExpiredExceptionMsg);
        } catch (UnsupportedJwtException | IllegalArgumentException | MalformedJwtException | SignatureException e) {
            String tokenInvalidExceptionMsg = "Authentication Failed: Invalid JWT token";
            throw new AuthenticationTokenException(tokenInvalidExceptionMsg);
        }
    }

    public static String extract(String header) {
        if(StringUtils.isEmpty(header)){
            throw new AuthenticationServiceException(AuthenticationConstant.AUTHENTICATION_FAILED + "No jwt token found in request header");
        }
        if(header.length() < AuthenticationConstant.JWT_TOKEN_PREFIX.length()){
            throw new AuthenticationServiceException(AuthenticationConstant.AUTHENTICATION_FAILED + "Invalid jwt token");
        }
        return header.substring(AuthenticationConstant.JWT_TOKEN_PREFIX.length());
    }
}
