package com.zhbit.xuexin.security.filter;

import com.zhbit.xuexin.security.common.AccessToken;
import com.zhbit.xuexin.security.common.JwtAuthenticationToken;
import com.zhbit.xuexin.security.common.JwtToken;
import com.zhbit.xuexin.security.constant.AuthenticationConstant;
import com.zhbit.xuexin.security.util.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationProcessFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationFailureHandler failureHandler;

    public JwtAuthenticationProcessFilter(AuthenticationFailureHandler failureHandler, RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String tokenHeader = httpServletRequest.getHeader(AuthenticationConstant.JWT_TOKEN_HEADER);
        AccessToken accessToken = new AccessToken(JwtTokenUtil.extract(tokenHeader));
//        JwtToken jwtToken = new JwtToken(JwtTokenUtil.extract(tokenHeader));
        return this.getAuthenticationManager().authenticate(new JwtAuthenticationToken(accessToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authResult);
        SecurityContextHolder.setContext(securityContext);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        super.unsuccessfulAuthentication(request, response, failed);
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
