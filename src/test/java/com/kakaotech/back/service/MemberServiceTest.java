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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @DisplayName("같은 member id를 가진 client가 동시에 save할 경우 1번은 성공, 2번은 실패")
    void saveMember_WhenTwoClientsUseSameId_OneShouldFail() throws InterruptedException {
        // Given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .memberId("duplicateId")
                .password("password")
                .gender("MALE")
                .ageGroup("20")
                .build();

        when(passwordEncoder.encode(memberRequestDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.existsByMemberId("duplicateId")).thenReturn(false).thenReturn(true);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0, Member.class);
            return member;
        });

        // 2개의 스레드가 동시에 실행하는 역할
        CountDownLatch latch = new CountDownLatch(1);

        // 두 개의 스레드를 사용하여 동일한 memberRequestDto로 saveMember를 호출
        ExecutorService executorService = Executors.newFixedThreadPool(2);


        Runnable task = () -> {
            try {
                latch.await();
                memberService.saveMember(memberRequestDto);
            } catch (AlreadyExistsException e) {
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Execute two concurrent tasks
        executorService.execute(task);
        executorService.execute(task);
        latch.countDown();
        executorService.shutdown();

        // Verifications
        verify(memberRepository, times(2)).existsByMemberId("duplicateId");
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}

package com.kakaotech.back.service;

import com.kakaotech.back.dto.member.MemberSurveyDto;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    Member member;

    @BeforeEach
    void setup() {
        member = Member.builder()
                .id("a123456")
                .age(30)
                .travelStyle1(1)
                .build();
    }

    @Test
    @DisplayName("여행 성향 변경")
    void testUpdateTravelStyle() {
        // Given
        MemberSurveyDto request = MemberSurveyDto.builder().travelStyle1(7).build();

        // When
        when(memberRepository.findById(anyString())).thenReturn(Optional.of(member));
        MemberSurveyDto response = memberService.updateSurvey(member.getId(), request);

        assertEquals(response.travelStyle1(), request.travelStyle1());
    }
}
