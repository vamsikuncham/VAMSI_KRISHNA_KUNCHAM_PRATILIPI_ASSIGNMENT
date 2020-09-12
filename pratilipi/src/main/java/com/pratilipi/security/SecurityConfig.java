package com.pratilipi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.Cipher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = false, proxyTargetClass = true)
@Order(1000)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private AuthenticationManager authenticationManager;

//    @Bean
//    public MethodSecurityExpressionHandler expressionHandler() {
//        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//        return expressionHandler;
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST, "/v2/users/refresh", "/v3/users/refresh");
    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        if (authenticationManager == null) {
//            authenticationManager = super.authenticationManagerBean();
//        }
//        return authenticationManager;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.httpBasic();
        //http.addFilterBefore(new MDCFilter(), FilterSecurityInterceptor.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.authorizeRequests().antMatchers(
                "/healthcheck/**",
                "/health",
                "/admin/**",
                "/v1/story/*",
                "/v1/user/*"
        ).permitAll();

        http.authorizeRequests().antMatchers("/**")
                .authenticated();
        // @formatter:on
    }

}

