package com.irum.memberservice.domain.member.util;

import com.irum.global.advice.exception.CommonException;
import com.irum.global.advice.exception.errorcode.GlobalErrorCode;
import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.entity.enums.Role;
import com.irum.memberservice.domain.member.domain.repository.MemberRepository;
import com.irum.memberservice.global.exception.errorcode.MemberErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Member getMemberById(Long memberId) {
        return memberRepository
                .findByMemberId(memberId)
                .orElseThrow(() -> new CommonException(MemberErrorCode.MEMBER_NOT_FOUND));
    } // 타 사용자의 정보 조회(MANAGER, MASTER 권한)

    public Member getMemberByEmail(String email) {
        return memberRepository
                .findMemberByEmail(email)
                .orElseThrow(() -> new CommonException(MemberErrorCode.MEMBER_NOT_FOUND));
    } // 로그인 된 유저 정보 조회

    public void validatePassword(String originalPassword, String newPassword, Member member) {
        if (!passwordEncoder.matches(originalPassword, member.getPassword()))
            throw new CommonException(MemberErrorCode.INVALID_PASSWORD);
        if (passwordEncoder.matches(newPassword, member.getPassword()))
            throw new CommonException(MemberErrorCode.DUPLICATED_PASSWORD);
    }

    public void assertPassword(String password, Member member) {
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new CommonException(MemberErrorCode.INVALID_PASSWORD);
    }

    public void validateNewOwnerRegistration(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            Role role = member.get().getRole();
            switch (role) {
                case OWNER -> throw new CommonException(MemberErrorCode.MEMBER_ALREADY_EXISTS);
                case CUSTOMER -> throw new CommonException(MemberErrorCode.OWNER_UPGRADE_REQUIRED);
            }
        }
    }

    public void assertMemberIsNotOwner(Member member) {
        if (member.getRole().equals(Role.OWNER))
            throw new CommonException(MemberErrorCode.ROLE_ALREADY_GRANTED);
    }

    public void assertEmailIsNotTaken(String email) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new CommonException(MemberErrorCode.MEMBER_ALREADY_EXISTS);
    }

    public void assertMemberIsCustomer(Member member) {
        Role role = member.getRole();
        if (!role.equals(Role.CUSTOMER)) {
            switch (role) {
                case OWNER -> throw new CommonException(MemberErrorCode.OWNER_CANNOT_WITHDRAW);
                case MANAGER -> throw new CommonException(MemberErrorCode.MANAGER_CANNOT_WITHDRAW);
            }
        }
    }

    public void assertMemberIsManager(Member member) {
        if (!member.getRole().equals(Role.MANAGER))
            throw new CommonException(MemberErrorCode.MEMBER_IS_NOT_MANAGER);
    }

    public void applyValidUpdate(Member member, String newName, String newContact) {
        if (!StringUtils.hasText(newName) && !StringUtils.hasText(newContact))
            throw new CommonException(GlobalErrorCode.EMPTY_REQUEST);
        if (StringUtils.hasText(newName)) member.updateName(newName);
        if (StringUtils.hasText(newContact)) member.updateContact(newContact);
    }
}
