package com.irum.memberservice.domain.member.service;

import com.irum.memberservice.domain.member.domain.entity.Member;
import com.irum.memberservice.domain.member.domain.repository.MemberRepository;
import com.irum.memberservice.domain.member.util.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberValidatorTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberValidator memberValidator;

    private Member mockCustomer;
    private Member mockOwner;
    private Member mockManager;

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
                "tester2",
                "010-1234-5678"
        );

        mockManager = Member.createManager(
                "manager@example.com",
                "1234",
                "tester3",
                "010-1234-5678"
        );

    }

    @Nested
    @DisplayName("조회 메서드 검증")
    class GetMemberTests {
        @Test
        @DisplayName("ID로 멤버 조회 성공")
        void getMemberById_success() {

            // Given
            Long memberId = 100L;
            given(memberRepository.findByMemberId(memberId)).willReturn(Optional.of(mockCustomer));

            // When
            Member foundMember = memberValidator.getMemberById(memberId);

            // Then
            assertThat(foundMember).isEqualTo(mockCustomer);
            verify(memberRepository).findByMemberId(memberId);
        }
    }

}
