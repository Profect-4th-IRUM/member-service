package com.irum.memberservice.global.config;

import com.irum.memberservice.domain.auth.service.AuthService;
import com.irum.memberservice.domain.auth.service.JwtTokenService;
import com.irum.memberservice.domain.member.service.ManagerService;
import com.irum.memberservice.domain.member.service.MemberService;
import com.irum.memberservice.global.util.CookieUtil;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public MemberService memberService() {
        return Mockito.mock(MemberService.class);
    }

    @Bean
    public ManagerService managerService() {
        return Mockito.mock(ManagerService.class);
    }

    @Bean
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }


    @Bean
    public CookieUtil cookieUtil() {
        return Mockito.mock(CookieUtil.class);
    }

    @Bean
    public JwtTokenService jwtTokenService() {
        return Mockito.mock(JwtTokenService.class);
    }

}
