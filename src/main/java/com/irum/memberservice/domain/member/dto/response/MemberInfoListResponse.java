package com.irum.memberservice.domain.member.dto.response;

import java.util.List;

public record MemberInfoListResponse(
        List<MemberInfoResponse> memberInfoList, Long nextCursor, boolean hasNext) {}
