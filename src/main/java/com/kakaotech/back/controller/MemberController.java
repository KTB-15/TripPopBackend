package com.kakaotech.back.controller;

import com.kakaotech.back.common.api.ApiResponse;
import com.kakaotech.back.dto.MemberRequestDto;
import com.kakaotech.back.dto.MemberResponseDto;
import com.kakaotech.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> existsByMemberId(@PathVariable String id) {
        boolean exists = memberService.existsByMemberId(id);
        return (exists)? ResponseEntity.ok(ApiResponse.success(true)) : ResponseEntity.ok(ApiResponse.success(false));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<MemberResponseDto>> saveMember(@RequestBody MemberRequestDto memberRequestDto) {

    }
}
