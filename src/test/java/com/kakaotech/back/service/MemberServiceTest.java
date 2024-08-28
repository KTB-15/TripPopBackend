package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.AlreadyExistsException;
import com.kakaotech.back.dto.member.MemberRequestDto;
import com.kakaotech.back.dto.member.MemberResponseDto;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("멤버를 등록할 때, 멤버가 이미 존재하는 경우 예외")
    void saveMember_WhenMemberIdAlreadyExists_ShouldThrowAlreadyExistsException() {
        // Given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .memberId("existingId")
                .password("password")
                .gender("MALE")
                .ageGroup("20")
                .build();

        when(memberRepository.existsByMemberId(memberRequestDto.getMemberId())).thenReturn(true);

        // When
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            memberService.saveMember(memberRequestDto);
        });

        // Then
        assertEquals("existingId는 이미 존재하는 ID 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("멤버를 등록할 때, 이미 등록한 멤버가 없어서 등록 성공")
    void saveMember_WhenMemberIdDoesNotExist_ShouldSaveMemberSuccessfully() {
        // Given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .memberId("newId")
                .password("password")
                .gender("MALE")
                .ageGroup("20")
                .build();

        when(memberRepository.existsByMemberId(memberRequestDto.getMemberId())).thenReturn(false);
        when(passwordEncoder.encode(memberRequestDto.getPassword())).thenReturn("encodedPassword");

        Member memberToSave = Member.builder()
                .memberId(memberRequestDto.getMemberId())
                .password("encodedPassword")
                .gender(Gender.MALE)
                .age(20)
                .build();

        Member savedMember = Member.builder()
                .memberId("newId")
                .password("encodedPassword")
                .gender(Gender.MALE)
                .age(20)
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        MemberResponseDto responseDto = memberService.saveMember(memberRequestDto);

        // Then
        assertNotNull(responseDto);
        assertEquals("newId", responseDto.getMemberId());
        assertEquals("MALE", responseDto.getGender());
        assertEquals(20, responseDto.getAgeGroup());

        // 중복된 memberId가 있는지 확인
        verify(memberRepository, times(1)).existsByMemberId(memberRequestDto.getMemberId());
        // password encoding 확인
        verify(passwordEncoder, times(1)).encode(memberRequestDto.getPassword());
    }
}
