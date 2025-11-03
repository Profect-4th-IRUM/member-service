package com.irum.memberservice.domain.member.domain.repository;

import com.irum.memberservice.domain.member.dto.response.MemberInfoResponse;
import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberInfoResponse> findMembersByCursor(Long lastMemberId, int pageSize);
}
