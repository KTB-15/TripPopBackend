package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.MemberException;
import com.kakaotech.back.dto.RegisterMemberDto;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import com.kakaotech.back.repository.SGGRepository;
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
    private final SGGRepository sggRepository;

    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public Member saveMember(RegisterMemberDto registerMemberDto) {
        if (memberRepository.existsByMemberId(registerMemberDto.getId())) {
            throw new MemberException(ErrorMessage.MEMBER_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .memberId(registerMemberDto.getId())
                .password(passwordEncoder.encode(registerMemberDto.getPassword()))
                .gender(Gender.valueOf(registerMemberDto.getGender()))
                .age(Integer.valueOf(registerMemberDto.getAgeGroup()))
                .build();

        return memberRepository.save(member);
    }

}
