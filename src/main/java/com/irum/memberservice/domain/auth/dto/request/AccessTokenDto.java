package com.irum.memberservice.domain.auth.dto.request;

import com.irum.memberservice.domain.member.domain.entity.enums.Role;

public record AccessTokenDto(Long memberId, Role role, String accessTokenValue) {
    public static AccessTokenDto of(Long memberId, Role role, String accessTokenValue) {
        return new AccessTokenDto(memberId, role, accessTokenValue);
    }
}
