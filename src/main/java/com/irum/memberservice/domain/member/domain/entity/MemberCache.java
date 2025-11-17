package com.irum.memberservice.domain.member.domain.entity;

import com.irum.memberservice.domain.member.domain.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "member")
public class MemberCache {
    @Id
    private Long memberId;
    private String email;
    private String name;
    private String contact;
    private Role role;

    @TimeToLive
    private long ttl;

    @Builder
    private MemberCache(Long memberId, String email, String name, String contact, Role role, long ttl) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.contact = contact;
        this.role = role;
        this.ttl = ttl;
    }
}
