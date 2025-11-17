package com.irum.memberservice.domain.member.service;

import com.irum.global.context.MemberAuthContext;
import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.repository.MemberRepository;
import com.irum.memberservice.global.util.MemberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberUtilTest {

    @InjectMocks
    private MemberUtil memberUtil;
    @Mock
    private MemberRepository memberRepository;
    @Captor
    private ArgumentCaptor<Member> memberCaptor;

    private Member mockMember;

    @BeforeEach
    void setUp() {
        mockMember = Member.createCustomer(
                "customer@example.com",
                "1234",
                "tester1",
                "010-1234-5678"
        );
    }

    @DisplayName("memberUtil")
    @Test
    void getCurrentMember_success(){
        //given
        Long expectedMemberId = 1L;
        try (MockedStatic<MemberAuthContext> mockedAuthContext = mockStatic(MemberAuthContext.class)) {
            mockedAuthContext.when(MemberAuthContext::getMemberId)
                    .thenReturn(expectedMemberId);
            given(memberRepository.findByMemberId(expectedMemberId)).willReturn(Optional.ofNullable(mockMember));

            //when
            Member currentMember = memberUtil.getCurrentMember();
            //then
            assertThat(currentMember).isNotNull();
            assertThat(currentMember.getEmail()).isEqualTo(mockMember.getEmail());
            System.out.println(currentMember.getMemberId());
            mockedAuthContext.verify(MemberAuthContext::getMemberId, times(1));
        }
    }
}
