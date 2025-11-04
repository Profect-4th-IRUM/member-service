package com.irum.memberservice.domain.member.Internal.controller;

import com.irum.memberservice.domain.member.Internal.service.MemberInternalService;
import com.irum.memberservice.openfeign.dto.request.MemberIdListDto;
import com.irum.memberservice.openfeign.dto.response.MemberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/members")
@RequiredArgsConstructor
public class MemberFeignController {
    private final MemberInternalService memberInternalService;

    @GetMapping("/{memberId}")
    public MemberDto getMember(@PathVariable("memberId") Long memberId){
        return memberInternalService.getMember(memberId);
    }
    @GetMapping()
    public List<MemberDto> getMember(@Valid @RequestBody MemberIdListDto request) {
        return memberInternalService.getMemberList(request);
    }
}
