package com.zhbit.xuexin.security.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:config/path-setting.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "path", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class PathConfig {

    private String loginPath;

    private String skipPathPattern;

    private String securityPathPattern;

    private String authenticatedPathAdmin;

    private String authenticatedPathStudent;

    private String authenticatedPathTeacher;

    private String authenticatedPathCompany;

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getSkipPathPattern() {
        return skipPathPattern;
    }

    public void setSkipPathPattern(String skipPathPattern) {
        this.skipPathPattern = skipPathPattern;
    }

    public String getSecurityPathPattern() {
        return securityPathPattern;
    }

    public void setSecurityPathPattern(String securityPathPattern) {
        this.securityPathPattern = securityPathPattern;
    }

    public String getAuthenticatedPathAdmin() {
        return authenticatedPathAdmin;
    }

    public void setAuthenticatedPathAdmin(String authenticatedPathAdmin) {
        this.authenticatedPathAdmin = authenticatedPathAdmin;
    }

    public String getAuthenticatedPathStudent() {
        return authenticatedPathStudent;
    }

    public void setAuthenticatedPathStudent(String authenticatedPathStudent) {
        this.authenticatedPathStudent = authenticatedPathStudent;
    }

    public String getAuthenticatedPathTeacher() {
        return authenticatedPathTeacher;
    }

    public void setAuthenticatedPathTeacher(String authenticatedPathTeacher) {
        this.authenticatedPathTeacher = authenticatedPathTeacher;
    }

    public String getAuthenticatedPathCompany() {
        return authenticatedPathCompany;
    }

    public void setAuthenticatedPathCompany(String authenticatedPathCompany) {
        this.authenticatedPathCompany = authenticatedPathCompany;
    }
}
