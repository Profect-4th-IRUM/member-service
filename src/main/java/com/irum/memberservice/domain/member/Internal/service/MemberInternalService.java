package com.irum.memberservice.domain.member.Internal.service;

import com.irum.memberservice.domain.member.util.MemberValidator;
import com.irum.memberservice.openfeign.dto.request.MemberIdListDto;
import com.irum.memberservice.openfeign.dto.response.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberInternalService {
    private final MemberValidator memberValidator;

    public MemberDto getMember(Long memberId) {
        return MemberDto.from(memberValidator.getMemberById(memberId));
    }

    public List<MemberDto> getMemberList(MemberIdListDto request) {
        return
                request.memberIdList().stream()
                        .map(memberValidator::getMemberById)
                        .map(MemberDto::from).toList();

    }
}
