package com.irum.memberservice.domain.member.Internal.service;

import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.util.MemberValidator;
import com.irum.openfeign.member.dto.request.MemberIdListDto;
import com.irum.openfeign.member.dto.response.MemberDto;
import com.irum.openfeign.member.enums.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberInternalService {
    private final MemberValidator memberValidator;

    public MemberDto getMember(Long memberId) {
        Member member = memberValidator.getMemberById(memberId);
        return new MemberDto(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getContact(),
                Role.valueOf(member.getRole().name()));
    }

    public List<MemberDto> getMemberList(MemberIdListDto request) {
        return request.memberIdList().stream()
                .map(memberValidator::getMemberById)
                .map(
                        m ->
                                new MemberDto(
                                        m.getMemberId(),
                                        m.getName(),
                                        m.getEmail(),
                                        m.getContact(),
                                        Role.valueOf(m.getRole().name())))
                .toList();
    }
}
