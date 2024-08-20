package com.kakaotech.back.service;

import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }
}
