package com.zhbit.xuexin.security.util;

import com.zhbit.xuexin.security.common.JwtConfig;
import com.zhbit.xuexin.security.common.JwtToken;
import com.zhbit.xuexin.security.common.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

//        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
//        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
//        claims.put("userInfo", userContext.getUser());

        Map<String, Object> claims = new HashMap<>(16);
        claims.put("sub", userContext.getUsername());
        claims.put("created", new Date());


        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS512");
        map.put("typ", "JWT");

        LocalDateTime nowTime = LocalDateTime.now();

        String token = Jwts.builder().setClaims(claims)
                .setHeader(map)
                .setIssuer("xuexin.service")
                .setIssuedAt(Date.from(nowTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(nowTime.plusMinutes(jwtConfig.getTokenExpireTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getTokenSigningKey())
                .compact();
     return new JwtToken(token, claims);
    }
}
