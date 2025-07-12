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
                        "/api/stu/update",
                        "/admin/login",
                        "/admin/update",
                        "/dormManager/login",
                        "/dormManager/update",
                        "/notice/homePageNotice",
                        "/notice/find",
                        "/notice/add",
                        "/notice/update",
                        "/notice/delete/**",
                        "/repair/orderNum",
                        "/repair/find/**",
                        "/repair/add",
                        "/repair/update",
                        "/repair/delete/**",
                        "/building/getBuildingName",
                        "/building/find",
                        "/building/add",
                        "/building/update",
                        "/building/delete/**",
                        "/room/noFullRoom",
                        "/room/selectHaveRoomStuNum",
                        "/room/getEachBuildingStuNum/**",
                        "/room/getMyRoom/**",
                        "/room/find",
                        "/room/add",
                        "/room/update",
                        "/room/delete/**",
                        "/room/checkRoomState/**",
                        "/room/checkBedState/**",
                        "/room/checkRoomExist/**",
                        "/room/judgeHadBed/**",
                        "/adjustRoom/find",
                        "/adjustRoom/add",
                        "/adjustRoom/update/**",
                        "/adjustRoom/delete/**",
                        "/stuInfo/find",
                        "/stuInfo/add",
                        "/stuInfo/update",
                        "/stuInfo/delete/**",
                        "/dormManagerInfo/find",
                        "/dormManagerInfo/add",
                        "/dormManagerInfo/update",
                        "/dormManagerInfo/delete/**",
                        "/visitorInfo/find",
                        "/visitorInfo/add",
                        "/visitorInfo/update",
                        "/visitorInfo/delete/**",
                        "/files/initAvatar/**",
                        "/files/uploadAvatar/**",
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