package com.kakaotech.back.controller;

import com.kakaotech.back.common.api.ApiResponse;
import com.kakaotech.back.dto.RegisterMemberDto;
import com.kakaotech.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> existsByMemberId(@PathVariable String id) {
        boolean exists = memberService.existsByMemberId(id);
        return (exists)? ResponseEntity.ok(ApiResponse.success(true)) : ResponseEntity.ok(ApiResponse.success(false));
    }

    @PostMapping
    public ResponseEntity<?> saveMember(@RequestBody RegisterMemberDto registerMemberDto) {
        try {
            memberService.saveMember(registerMemberDto);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (RuntimeException e) {
            System.err.println("Error in saveMember: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
