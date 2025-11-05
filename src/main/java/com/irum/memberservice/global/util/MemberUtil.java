package com.irum.memberservice.global.util;

import com.irum.global.advice.exception.CommonException;
import com.irum.global.context.MemberAuthContext;
import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.repository.MemberRepository;
import com.irum.memberservice.global.exception.errorcode.AuthErrorCode;
import com.irum.memberservice.global.exception.errorcode.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberUtil {

    private final MemberRepository memberRepository;

    public Member getCurrentMember() {
        return memberRepository
                .findByMemberId(getCurrentMemberId())
                .orElseThrow(() -> new CommonException(AuthErrorCode.AUTHENTICATION_NOT_FOUND));
    } // 로그인 된 유저 정보 조회

    public void assertMemberResourceAccess(Member member) {
        if (!member.getMemberId().equals(getCurrentMember().getMemberId()))
            throw new CommonException(MemberErrorCode.UNAUTHORIZED_ACCESS);
    }

    private Long getCurrentMemberId() {
        return MemberAuthContext.getMemberId();
    } // 로그인 된 아이디 반환
}
