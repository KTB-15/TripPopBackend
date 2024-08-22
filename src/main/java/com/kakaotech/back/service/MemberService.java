package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.MemberException;
import com.kakaotech.back.dto.member.MemberResponseDto;
import com.kakaotech.back.dto.member.MemberRequestDto;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByMemberId(memberRequestDto.getMemberId())) {
            throw new MemberException(ErrorMessage.MEMBER_ALREADY_EXISTS);
        }
        System.out.println("memberRequestDto.memberId = " + memberRequestDto.getMemberId());
        System.out.println("memberRequestDto.password = " + memberRequestDto.getPassword());
        Member member = Member.builder()
                .memberId(memberRequestDto.getMemberId())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .gender(Gender.valueOf(memberRequestDto.getGender()))
                .age(Integer.valueOf(memberRequestDto.getAgeGroup()))
                .build();

        Member savedMember = memberRepository.save(member);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(savedMember.getMemberId())
                .memberId(savedMember.getMemberId())
                .gender(savedMember.getGender().toString())
                .ageGroup(savedMember.getAge())
                .build();

        return responseDto;
    }

}
