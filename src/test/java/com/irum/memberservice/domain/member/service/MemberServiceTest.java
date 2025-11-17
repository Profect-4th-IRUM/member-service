package com.irum.memberservice.domain.member.service;


import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.entity.enums.Role;
import com.irum.memberservice.domain.member.domain.repository.MemberRepository;
import com.irum.memberservice.domain.member.dto.request.MemberCreateRequest;
import com.irum.memberservice.domain.member.dto.request.MemberInfoUpdateRequest;
import com.irum.memberservice.domain.member.util.MemberValidator;
import com.irum.memberservice.global.util.MemberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberValidator memberValidator;
    @Mock
    private MemberUtil memberUtil;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Member> memberCaptor;

    private Member mockCustomer;
    private Member mockOwner;
    private MemberCreateRequest customerCreateRequest;
    private MemberCreateRequest ownerCreateRequest;
    private MemberInfoUpdateRequest memberInfoUpdateRequest;

    @BeforeEach
    void setUp(){
        mockCustomer = Member.createCustomer(
                "customer@example.com",
                "1234",
                "tester1",
                "010-1234-5678"
        );
        mockOwner = Member.createCustomer(
                "owner@example.com",
                "1234",
                "tester1",
                "010-1234-5678"
        );

    }


    @DisplayName("고객회원 생성 테스트")
    @Test
    void createCustomer_success(){
        //given
        customerCreateRequest = new MemberCreateRequest(
                mockCustomer.getEmail(),
                mockCustomer.getPassword(),
                mockCustomer.getName(),
                mockCustomer.getContact()
        );
        willDoNothing().given(memberValidator).assertEmailIsNotTaken(customerCreateRequest.email());
        given(passwordEncoder.encode(customerCreateRequest.password())).willReturn("encodedPassword");
        //when
        memberService.createCustomer(customerCreateRequest);
        //then
        verify(memberRepository, times(1)).save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @DisplayName("판매자회원 생성 테스트")
    @Test
    void createOwner_success(){
        //given
        ownerCreateRequest = new MemberCreateRequest(
                mockOwner.getEmail(),
                mockOwner.getPassword(),
                mockOwner.getName(),
                mockOwner.getContact()
        );
        willDoNothing().given(memberValidator).validateNewOwnerRegistration(ownerCreateRequest.email());
        given(passwordEncoder.encode(ownerCreateRequest.password())).willReturn("encodedPassword");
        //when
        memberService.createOwner(ownerCreateRequest);
        //then
        verify(memberRepository, times(1)).save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getRole()).isEqualTo(Role.OWNER);
    }

    @DisplayName("회원 조회 테스트")
    @Test
    void findMemberInfo_success(){
        //given
        given(memberUtil.getCurrentMember()).willReturn(mockCustomer);
        //when
        memberService.findMemberInfo();
        //then
        verify(memberRepository, times(1)).save(memberCaptor.capture());
        Member foundMember = memberCaptor.getValue();
        assertThat(foundMember).isEqualTo(mockCustomer);
    }

    @DisplayName("회원 조회 테스트")
    @Test
    void changeMemberNameAndContact_success(){
        //given
        memberInfoUpdateRequest = new MemberInfoUpdateRequest(mockCustomer.getName(),mockCustomer.getContact());
        given(memberUtil.getCurrentMember()).willReturn(mockCustomer);
        //when
        memberService.findMemberInfo();
        //then
        verify(memberRepository, times(1)).save(memberCaptor.capture());
        Member foundMember = memberCaptor.getValue();
        assertThat(foundMember).isEqualTo(mockCustomer);
    }
}
