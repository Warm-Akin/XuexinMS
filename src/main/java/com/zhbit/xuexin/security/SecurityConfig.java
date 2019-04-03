package com.zhbit.xuexin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhbit.xuexin.security.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    MyAuthenticationProvider authenticationProvider;

    private static final String loginPath = "/xuexin/user/login";


//    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 把不需要认证的接口暴露出去
                .antMatchers(loginPath).permitAll()
                .antMatchers("/xuexin/anonymous/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/xuexin/admin/**").hasRole("ADMIN")
                .antMatchers("/xuexin/student/**").hasRole("STUDENT")
                .antMatchers("/xuexin/company/**").hasRole("COMPANY")
                .and()
                .addFilterBefore(buildLoginFilter(), UsernamePasswordAuthenticationFilter.class);
//                .anyRequest().authenticated()
//                .and().headers().cacheControl();
//        super.configure(http);
    }

    private LoginFilter buildLoginFilter() {
        LoginFilter loginFilter = new LoginFilter(loginPath, successHandler, failureHandler, objectMapper);
        loginFilter.setAuthenticationManager(this.authenticationManager);
        return loginFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
