package com.kakaotech.back.service;

import com.kakaotech.back.dto.RegisterMemberDto;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.SGG;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public Member saveMember(RegisterMemberDto registerMemberDto) {
        if (memberRepository.existsByMemberId(registerMemberDto.getId())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // TODO
        // ssg id값 어떻게 가져옴?
        // age
        // gender 수정
        SGG sgg = SGG.builder()
                .sidoName(registerMemberDto.getArea())
                .sggName(registerMemberDto.getSubArea())
                .build();

        Member member = Member.builder()
                .memberId(registerMemberDto.getId())
                .password(passwordEncoder.encode(registerMemberDto.getPassword()))
                .gender(Gender.valueOf(registerMemberDto.getGender()))
                .age(registerMemberDto.getAgeGroup())
                .sgg(sgg)
                .build();

        return memberRepository.save(member);
    }
}
