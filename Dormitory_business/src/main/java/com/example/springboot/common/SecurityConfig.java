package com.example.springboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/stu/login",
                        "/api/stu/register",
                        "/api/stu/stuNum",
                        "/admin/login",
                        "/dormManager/login",
                        "/notice/homePageNotice",
                        "/repair/orderNum",
                        "/building/getBuildingName",
                        "/room/noFullRoom",
                        "/room/selectHaveRoomStuNum",
                        "/room/getEachBuildingStuNum/**",
                        "/room/getMyRoom/**",
                        "/adjustRoom/find",
                        "/repair/find/**",
                        "/main/loadIdentity",
                        "/main/loadUserInfo",
                        "/main/signOut"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        return http.build();
    }
}