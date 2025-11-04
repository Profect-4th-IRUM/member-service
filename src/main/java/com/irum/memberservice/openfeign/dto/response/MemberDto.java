package com.irum.memberservice.openfeign.dto.response;

import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.entity.enums.Role;

public record MemberDto(Long memberId, String name, String email, String contact, Role role) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getContact(),
                member.getRole());
    }
}
