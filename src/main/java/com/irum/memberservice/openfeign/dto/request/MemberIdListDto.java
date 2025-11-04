package com.irum.memberservice.openfeign.dto.request;

import jakarta.validation.constraints.Size;

import java.util.List;

public record MemberIdListDto(
        @Size(min = 1, message = "멤버 리스트는 최소 1명 이상이어야 합니다.")
        List<Long> memberIdList) {}
