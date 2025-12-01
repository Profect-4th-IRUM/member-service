package com.irum.memberservice.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record MemberLoginResponse(String accessToken, @JsonIgnore String refreshToken) {
    public static MemberLoginResponse of(String accessToken, String refreshToken) {
        return new MemberLoginResponse(accessToken, refreshToken);
    }
}
