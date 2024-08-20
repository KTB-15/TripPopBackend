package com.kakaotech.back.controller;

import com.kakaotech.back.dto.RegisterMemberDto;
import com.kakaotech.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> existsByMemberId(@PathVariable String id) {
        boolean exists = memberService.existsByMemberId(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> registerMember(@RequestBody RegisterMemberDto registerMemberDto) {
        try {
            memberService.saveMember(registerMemberDto);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
