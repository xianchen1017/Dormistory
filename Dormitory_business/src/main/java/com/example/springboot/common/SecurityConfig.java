package com.example.springboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 允许所有OPTIONS请求
                .antMatchers(
                        "/api/stu/login",
                        "/api/stu/register",
                        "/api/stu/sendVerificationCode",
                        "/api/stu/stuNum",
                        "/api/stu/update",
                        "/api/stu/exist/**",
                        "/api/admin/login",
                        "/api/admin/update",
                        "/api/dormManager/login",
                        "/api/dormManager/update",
                        "/api/notice/homePageNotice",
                        "/api/notice/find",
                        "/api/notice/add",
                        "/api/notice/update",
                        "/api/notice/delete/**",
                        "/api/repair/orderNum",
                        "/api/repair/find/**",
                        "/api/repair/add",
                        "/api/repair/update",
                        "/api/repair/delete/**",
                        "/api/building/getBuildingName",
                        "/api/building/find",
                        "/api/building/add",
                        "/api/building/update",
                        "/api/building/delete/**",
                        "/api/room/noFullRoom",
                        "/api/room/selectHaveRoomStuNum",
                        "/api/room/getEachBuildingStuNum/**",
                        "/api/room/getMyRoom/**",
                        "/api/room/find",
                        "/api/room/add",
                        "/api/room/update",
                        "/api/room/delete/**",
                        "/api/room/checkRoomState/**",
                        "/api/room/checkBedState/**",
                        "/api/room/checkRoomExist/**",
                        "/api/room/judgeHadBed/**",
                        "/api/adjustRoom/find",
                        "/api/adjustRoom/findAll",
                        "/api/adjustRoom/add",
                        "/api/adjustRoom/update/**",
                        "/api/adjustRoom/delete/**",
                        "/api/stu/find",
                        "/api/stu/add",
                        "/api/stu/update",
                        "/api/stu/delete/**",
                        "/api/dormManager/find",
                        "/api/dormManager/add",
                        "/api/dormManager/update",
                        "/api/dormManager/delete/**",
                        "/api/visitor/find",
                        "/api/visitor/add",
                        "/api/visitor/update",
                        "/api/visitor/delete/**",
                        "/api/files/initAvatar/**",
                        "/api/files/uploadAvatar/**",
                        "/api/main/loadIdentity",
                        "/api/main/loadUserInfo",
                        "/api/main/signOut",
                        // 新增的晚归管理接口放行规则
                        "/api/come-back-late/add",
                        "/api/come-back-late/delete/**",
                        "/api/come-back-late/update",
                        "/api/come-back-late/find",
                        "/api/come-back-late/find-by-student",
                        "/api/report/**",
                        "/api/test/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        return http.build();
    }
}