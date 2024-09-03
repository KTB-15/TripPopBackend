package com.kakaotech.back.service;


import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.RecommendResDto;
import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.repository.FavouriteRepository;
import com.kakaotech.back.repository.MemberRepository;
import com.kakaotech.back.service.place.PlaceService;
import com.kakaotech.back.util.SecurityUtil;
import com.kakaotech.back.vo.TravelerInfoVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

// 메인 페이지에 추천 여행지를 제공해주는 서비스
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendService {
    private final PlaceService placeService;
    private final MemberRepository memberRepository;
    private final FavouriteRepository favouriteRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 추천 여행지 관련 정보
    public List<RecommendResDto> getRecommendedPlaceInfo(String memberId) {
        // 여행 성향에 맞는 추천 여행지 수신
        TravelerInfoVO travelerInfo = getTravelerInfo(memberId);
        List<Place> recommended = getRecommendedPlaces(travelerInfo);

        // 추천받은 여행지에 대한 정보, 이미지 및 즐겨찾기 상태 여부 조회
        List<Long> placeIds = recommended.stream().map(Place::getId).toList();
        List<byte[]> placeImages = placeIds.parallelStream().map(placeId -> new AbstractMap.SimpleEntry<>(placeId, placeService.getPlaceImage(placeId)))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
        List<Favourite> alreadyFavourite = favouriteRepository.findByMemberIdAndPlaceIdIn(memberId, placeIds).orElse(List.of());
        List<Boolean> isAlreadyLiked = isAlreadyFavouriteForPlace(alreadyFavourite, placeIds);

        // 응답 준비
        ArrayList<RecommendResDto> result = new ArrayList<>();
        for (int i = 0; i < placeIds.size(); i += 1){
            result.add(RecommendResDto.of(recommended.get(i), placeImages.get(i), isAlreadyLiked.get(i)));
        }
        return result;
    }

    // TODO: AI 서버로부터 추천 여행지 수신
    private List<Place> getRecommendedPlaces (TravelerInfoVO travelerInfo){
        List<Long> placeIds = List.of(50L, 51L, 52L, 53L, 54L, 56L, 57L, 58L, 59L, 60L);
        return placeService.getPlacesInOrder(placeIds);
    }

    // 사용자 여행 성향
    private TravelerInfoVO getTravelerInfo(String memberId) {
        Member member = memberRepository.findOneWithAuthoritiesByMemberId(memberId).orElseThrow(
                () -> new NotFoundException(NotFoundException.messageWithInfo(memberId))
        );
        return TravelerInfoVO.from(member);
    }

    // 추천받은 여행지를 이미 즐겨찾기 했는지 확인
    private List<Boolean> isAlreadyFavouriteForPlace(List<Favourite> favourites, List<Long> placeIds){
        List<Long> likedPlaceIds = favourites.stream()
                .map(Favourite::getId)
                .toList();
        return placeIds.stream()
                .map(likedPlaceIds::contains)
                .toList();
    }
}
