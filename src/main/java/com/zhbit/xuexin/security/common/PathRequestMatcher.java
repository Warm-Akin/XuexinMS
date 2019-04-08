package com.zhbit.xuexin.security.common;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class PathRequestMatcher implements RequestMatcher {

    private RequestMatcher skipMatcherPattern;

    private RequestMatcher securedMatcherPattern;

    public PathRequestMatcher(String skipPathPattern, String securityPathPattern) {
        skipMatcherPattern = new AntPathRequestMatcher(skipPathPattern);
        securedMatcherPattern = new AntPathRequestMatcher(securityPathPattern);
    }

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        return skipMatcherPattern.matches(httpServletRequest) ? false : securedMatcherPattern.matches(httpServletRequest);
    }
}
