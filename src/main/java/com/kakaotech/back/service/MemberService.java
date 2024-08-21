package com.kakaotech.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotech.back.dto.RegisterMemberDto;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.SGG;
import com.kakaotech.back.repository.MemberRepository;
import com.kakaotech.back.repository.SGGRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SGGRepository sggRepository;
    private Map<String, String> sidoMap;
    private Map<String, String> sggMap;

    @Bean
    public void init() throws IOException {
        // Load JSON data
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sgg.json");
        if (inputStream == null) {
            throw new IllegalArgumentException("sgg.json file not found in resources");
        }
        Map<String, Map<String, String>> jsonData = objectMapper.readValue(inputStream, Map.class);
        this.sidoMap = jsonData.get("SIDO");
        this.sggMap = jsonData.get("SGG");
    }

    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public Member saveMember(RegisterMemberDto registerMemberDto) {
        if (memberRepository.existsByMemberId(registerMemberDto.getId())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        SGG sgg = SGG.builder()
                .id(sidoMap.get(registerMemberDto.getArea())+sggMap.get(registerMemberDto.getSubArea()))
                .sidoName(registerMemberDto.getArea())
                .sggName(registerMemberDto.getSubArea())
                .build();
        sgg = sggRepository.save(sgg);

        Member member = Member.builder()
                .memberId(registerMemberDto.getId())
                .password(passwordEncoder.encode(registerMemberDto.getPassword()))
                .gender(Gender.valueOf(registerMemberDto.getGender()))
                .age(Integer.valueOf(registerMemberDto.getAgeGroup()))
                .sgg(sgg)
                .build();

        return memberRepository.save(member);
    }

}
