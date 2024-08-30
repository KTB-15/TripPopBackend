package com.kakaotech.back.controller;

import com.kakaotech.back.dto.member.MemberDto;
import com.kakaotech.back.dto.member.MemberRequestDto;
import com.kakaotech.back.dto.member.MemberResponseDto;
import com.kakaotech.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsByMemberId(@PathVariable String id) {
        return ResponseEntity.ok(memberService.existsByMemberId(id));
    }

    @PostMapping("/join")
    public ResponseEntity<MemberResponseDto> saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.saveMember(memberRequestDto);
        URI location = URI.create("/member/" + memberResponseDto.getId());
        return ResponseEntity.created(location).body(memberResponseDto);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMemberInfo() {
        return ResponseEntity.ok(memberService.getMemberWithAuthorities());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMemberInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getMemberWithAuthorities(username));
    }
}
