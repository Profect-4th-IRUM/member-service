package com.irum.memberservice.domain.auth.service;

import com.irum.memberservice.domain.auth.domain.repository.RefreshTokenRepository;
import com.irum.memberservice.domain.auth.dto.request.MemberLoginRequest;
import com.irum.memberservice.domain.auth.dto.response.MemberLoginResponse;
import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.util.MemberValidator;
import com.irum.memberservice.global.util.CookieUtil;
import com.irum.memberservice.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberValidator memberValidator;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;
    private final MemberUtil memberUtil;

    public MemberLoginResponse processMemberLogin(MemberLoginRequest request) {
        Member member = memberValidator.getMemberByEmail(request.email());
        memberValidator.assertPassword(request.password(), member);
        String accessToken =
                jwtTokenService.createAccessToken(member.getMemberId(), member.getRole());
        String refreshToken = jwtTokenService.createRefreshToken(member.getMemberId());
        return MemberLoginResponse.of(accessToken, refreshToken);
    }

    public HttpHeaders processMemberLogout() {
        Member member = memberUtil.getCurrentMember();
        refreshTokenRepository.deleteById(member.getMemberId());
        return cookieUtil.deleteRefreshTokenCookie();
    }
}
