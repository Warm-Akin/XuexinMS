package com.zhbit.xuexin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.UserRepository;
import com.zhbit.xuexin.security.common.*;
import com.zhbit.xuexin.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

//        JwtToken accessToken = (JwtToken) authentication.getCredentials();
        AccessToken accessToken = (AccessToken) authentication.getCredentials();
        Claims claims = tokenUtil.parseClaims(jwtConfig.getTokenSigningKey(), accessToken.getRawToken()).getBody();
        String username = claims.getSubject();
        List<String> scopes = claims.get("scopes", List.class);
        User userInfo = null;
        try {
            String userId = objectMapper.writeValueAsString(claims.get("userId"));
            userInfo = userRepository.findById(userId.replaceAll("\"", "")).orElse(null);
        } catch (IOException e) {
//            logger.error("Error processing userInfos parsed from jwt token");
            e.printStackTrace();
        }

        List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

//        sessionManager(scopes.get(0), username);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(new UserContext(username, userInfo, authorities), authorities);
        authenticationToken.setAccessToken(accessToken);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
