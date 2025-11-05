package com.irum.memberservice.domain.member.Internal.controller;

import com.irum.memberservice.domain.member.Internal.service.MemberInternalService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import openfeign.member.dto.request.MemberIdListDto;
import openfeign.member.dto.response.MemberDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/members")
@RequiredArgsConstructor
public class MemberFeignController {
    private final MemberInternalService memberInternalService;

    @GetMapping("/{memberId}")
    public MemberDto getMember(@PathVariable("memberId") Long memberId) {
        return memberInternalService.getMember(memberId);
    }

    @GetMapping()
    public List<MemberDto> getMember(@Valid @RequestBody MemberIdListDto request) {
        return memberInternalService.getMemberList(request);
    }
}
