package com.kakaotech.back.service;


import com.kakaotech.back.dto.RecommendReqDto;
import com.kakaotech.back.dto.RecommendResDto;
import com.kakaotech.back.service.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

// 메인 페이지에 추천 여행지를 제공해주는 서비스
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final PlaceService placeService;
    private final MemberService memberService;
    private final FavouriteService favouriteService;

    // 추천 여행지 관련 정보
    public List<RecommendResDto> getRecommendedPlaceInfo(RecommendReqDto dto){
        return Collections.singletonList(RecommendResDto.builder().build());
    }
}
