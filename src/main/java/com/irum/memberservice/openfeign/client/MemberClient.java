package com.irum.memberservice.openfeign.client;

import com.irum.memberservice.openfeign.dto.request.MemberIdListDto;
import com.irum.memberservice.openfeign.dto.response.MemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MEMBER-SERVICE")
public interface MemberClient {

    @GetMapping("/internal/members/{memberId}")
    MemberDto getMember(@PathVariable("memberId") String memberId);

    @GetMapping("/internal/members")
    MemberDto getMemberList(MemberIdListDto request);
}
