package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.AlreadyExistsException;
import com.kakaotech.back.dto.member.MemberResponseDto;
import com.kakaotech.back.dto.member.MemberRequestDto;
import com.kakaotech.back.entity.Authority;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.AuthorityRepository;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByMemberId(memberRequestDto.getMemberId())) {
            throw new AlreadyExistsException(memberRequestDto.getMemberId()+"는 이미 존재하는 ID 입니다.");
        }

        if(!authorityRepository.existsByAuthorityName("ROLE_USER")) {
            authorityRepository.save(new Authority("ROLE_USER"));
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .memberId(memberRequestDto.getMemberId())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .gender(Gender.valueOf(memberRequestDto.getGender()))
                .age(Integer.valueOf(memberRequestDto.getAgeGroup()))
                .authorities(Collections.singleton(authority))
                .activated(true)
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
