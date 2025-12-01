package com.irum.memberservice.domain.member.controller;

import com.irum.memberservice.domain.member.dto.request.MemberCreateRequest;
import com.irum.memberservice.domain.member.dto.request.MemberInfoUpdateRequest;
import com.irum.memberservice.domain.member.dto.request.MemberPasswordUpdateRequest;
import com.irum.memberservice.domain.member.dto.response.MemberInfoListResponse;
import com.irum.memberservice.domain.member.dto.response.MemberInfoResponse;
import com.irum.memberservice.domain.member.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/signup")
    public ResponseEntity<Void> createManagerAccount(
            @Valid @RequestBody MemberCreateRequest request) {
        log.info("매니저 계정 생성 요청: {}", request);
        managerService.createManager(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{managerId}/info")
    public MemberInfoResponse getManagerInfo(@PathVariable Long managerId) {
        return managerService.findManagerInfo(managerId);
    }

    @GetMapping
    public MemberInfoListResponse getManagerInfoList(
            @RequestParam(name = "lastId", required = false) Long lastMemberId,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize) {
        return managerService.findManagerInfoList(lastMemberId, pageSize);
    }

    @PatchMapping("/{managerId}/info")
    public ResponseEntity<Void> updateManagerInfo(
            @PathVariable Long managerId, @RequestBody MemberInfoUpdateRequest request) {
        log.info("매니저 개인정보 수정 요청: {}", request);
        managerService.changeManagerNameAndContact(managerId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{managerId}/password")
    public ResponseEntity<Void> updateManagerPassword(
            @PathVariable Long managerId, @Valid @RequestBody MemberPasswordUpdateRequest request) {
        log.info("매니저 비밀번호 변경 요청: {}", request);
        managerService.changeManagerPassword(managerId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long managerId) {
        managerService.removeManager(managerId);
        return ResponseEntity.noContent().build();
    }
}
