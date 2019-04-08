package com.zhbit.xuexin.security.common;

import com.zhbit.xuexin.model.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserContext{

    private String username;

    private User user;

    private List<GrantedAuthority> authorities;

    public UserContext() {
    }

    public UserContext(String username, User user, List<GrantedAuthority> authorities) {
        this.username = username;
        this.user = user;
        this.authorities = authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
