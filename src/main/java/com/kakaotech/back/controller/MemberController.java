package com.kakaotech.back.controller;

import com.kakaotech.back.dto.member.MemberRequestDto;
import com.kakaotech.back.dto.member.MemberResponseDto;
import com.kakaotech.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsByMemberId(@PathVariable String id) {
        boolean exists = memberService.existsByMemberId(id);
        return (exists)? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @PostMapping("/join")
    public ResponseEntity<MemberResponseDto> saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.saveMember(memberRequestDto);
        URI location = URI.create("/members/" + memberResponseDto.getId());
        return ResponseEntity.created(location).body(memberResponseDto);
    }
}
