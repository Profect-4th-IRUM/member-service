package com.irum.memberservice.domain.member.dto.response;

import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.entity.enums.Role;

public record MemberInfoResponse(
        Long memberId, String email, String name, String contact, Role role) {
    public static MemberInfoResponse createMemberInfoResponse(Member member) {
        return new MemberInfoResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getName(),
                member.getContact(),
                member.getRole());
    }
}
