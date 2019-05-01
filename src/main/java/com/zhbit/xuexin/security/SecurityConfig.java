package com.zhbit.xuexin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhbit.xuexin.security.common.AuthenticationEntryPoint;
import com.zhbit.xuexin.security.common.CorsFilter;
import com.zhbit.xuexin.security.common.PathConfig;
import com.zhbit.xuexin.security.common.PathRequestMatcher;
import com.zhbit.xuexin.security.filter.JwtAuthenticationProcessFilter;
import com.zhbit.xuexin.security.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Autowired
    AuthenticationFailureHandler failureHandler;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    LoginAuthenticationProvider loginAuthenticationProvider;

    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    PathConfig pathConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                // 把不需要认证的接口暴露出去
                .antMatchers(pathConfig.getLoginPath()).permitAll()
                .antMatchers(pathConfig.getSkipPathPattern()).permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers(pathConfig.getAuthenticatedPathAdmin()).hasRole("ADMIN")
                .antMatchers(pathConfig.getAuthenticatedPathStudent()).hasRole("STUDENT")
                .antMatchers(pathConfig.getAuthenticatedPathTeacher()).hasRole("TEACHER")
                .antMatchers(pathConfig.getAuthenticatedPathCompany()).hasRole("COMPANY")
                .antMatchers(pathConfig.getSecurityPathPattern()).authenticated()
                .and()
                .addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtAuthenticationProcessFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private LoginFilter buildLoginFilter() {
        LoginFilter loginFilter = new LoginFilter(pathConfig.getLoginPath(), successHandler, failureHandler, objectMapper);
        loginFilter.setAuthenticationManager(this.authenticationManager);
        return loginFilter;
    }

    private JwtAuthenticationProcessFilter buildJwtAuthenticationProcessFilter() {
        PathRequestMatcher matcher = new PathRequestMatcher(pathConfig.getSkipPathPattern(), pathConfig.getSecurityPathPattern());
        JwtAuthenticationProcessFilter filter = new JwtAuthenticationProcessFilter(failureHandler, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
