package com.kakaotech.back.controller;

import com.kakaotech.back.dto.RecommendResDto;
import com.kakaotech.back.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;

    // 사용자 아이디 이용
    public List<RecommendResDto> getRecommendedPlaceInfo (@AuthenticationPrincipal UserDetails userDetails){
        return recommendService.getRecommendedPlaceInfo(userDetails.getUsername());
    }
}
