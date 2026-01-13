package com.irum.memberservice.global.util;

import com.irum.global.advice.exception.CommonException;
import com.irum.global.context.MemberAuthContext;
import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.entity.MemberCache;
import com.irum.memberservice.domain.member.domain.repository.MemberCacheRepository;
import com.irum.memberservice.domain.member.domain.repository.MemberRepository;
import com.irum.memberservice.global.exception.errorcode.AuthErrorCode;
import com.irum.memberservice.global.infrastructure.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberUtil {

    private final MemberRepository memberRepository;
    private final MemberCacheRepository memberCacheRepository;
    private final RedisProperties redisProperties;

    public Member getCurrentMember() {
        Long currentMemberId = getCurrentMemberId();
        MemberCache memberCache = memberCacheRepository.findById(currentMemberId).orElse(null);
        if (memberCache != null) return memberCache.getMember();
        Member member =
                memberRepository
                        .findByMemberId(getCurrentMemberId())
                        .orElseThrow(
                                () -> new CommonException(AuthErrorCode.AUTHENTICATION_NOT_FOUND));
        memberCacheRepository.save(
                MemberCache.create(member.getMemberId(), member, redisProperties.cacheTtl()));
        return member;
    } // 로그인 된 유저 정보 조회

    private Long getCurrentMemberId() {
        return MemberAuthContext.getMemberId();
    } // 로그인 된 아이디 반환
}
