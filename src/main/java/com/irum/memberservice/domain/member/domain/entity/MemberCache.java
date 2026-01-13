package com.irum.memberservice.domain.member.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "member")
@NoArgsConstructor
public class MemberCache {

    @Id private Long memberId;

    private Member member;

    @TimeToLive private long ttl;

    @Builder(access = AccessLevel.PRIVATE)
    private MemberCache(Long memberId, Member member, long ttl) {
        this.memberId = memberId;
        this.member = member;
        this.ttl = ttl;
    }

    public static MemberCache create(Long memberId, Member member, long ttl) {
        return MemberCache.builder().memberId(memberId).member(member).ttl(ttl).build();
    }
}
