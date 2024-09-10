package com.kakaotech.back.service;


import com.kakaotech.back.common.exception.GoogleApiException;
import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.RecommendResDto;
import com.kakaotech.back.dto.recommendation.RecReqDto;
import com.kakaotech.back.dto.recommendation.RecResDto;
import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.repository.FavouriteRepository;
import com.kakaotech.back.repository.MemberRepository;
import com.kakaotech.back.service.place.PlaceService;
import com.kakaotech.back.util.SecurityUtil;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.vo.TravelerInfoVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

// 메인 페이지에 추천 여행지를 제공해주는 서비스
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendService {
    private final PlaceService placeService;
    private final MemberRepository memberRepository;
    private final FavouriteRepository favouriteRepository;
    private final RestClient restClient;

    @Value("${ai.server.url}")
    private String AI_SERVER_URL;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 추천 여행지 관련 정보
    public List<RecommendResDto> getRecommendedPlaceInfo(String memberId) {
        // 여행 성향에 맞는 추천 여행지 수신
        Member member = memberRepository.findOneWithAuthoritiesByMemberId(memberId).orElseThrow(
                () -> new NotFoundException(NotFoundException.messageWithInfo(memberId))
        );
        RecReqDto recReqDto = RecReqDto.from(member);
        List<Place> recommended = getRecommendedPlaces(recReqDto);

        // 추천받은 여행지에 대한 정보, 이미지 및 즐겨찾기 상태 여부 조회
        List<Long> placeIds = recommended.stream().map(Place::getId).toList();
        List<byte[]> placeImages = placeIds.parallelStream().map(placeId -> new AbstractMap.SimpleEntry<>(placeId, placeService.getPlaceImage(placeId)))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
        List<Favourite> alreadyFavourite = favouriteRepository.findByMemberIdAndPlaceIdIn(member.getId(), placeIds).orElse(List.of());
        List<Boolean> isAlreadyLiked = isAlreadyFavouriteForPlace(alreadyFavourite, placeIds);

        // 응답 준비
        ArrayList<RecommendResDto> result = new ArrayList<>();
        for (int i = 0; i < placeIds.size(); i += 1) {
            result.add(RecommendResDto.of(recommended.get(i), placeImages.get(i), isAlreadyLiked.get(i)));
        }
        return result;
    }

    private List<Place> getRecommendedPlaces(TravelerInfoVO travelerInfo) {
        List<Long> placeIds = List.of(50L, 51L, 52L, 53L, 54L, 56L, 57L, 58L, 59L, 60L);
        return placeService.getPlacesInOrder(placeIds);
    }

    private List<Place> getRecommendedPlaces(RecReqDto dto) {
        RecResDto response = fetchRecommendedPlaces(dto);
        return placeService.getPlacesByAreaNames(response.recommended_places());
    }

    // 추천받은 여행지를 이미 즐겨찾기 했는지 확인
    private List<Boolean> isAlreadyFavouriteForPlace(List<Favourite> favourites, List<Long> placeIds) {
        List<Long> likedPlaceIds = favourites.stream()
                .map(f -> f.getPlace().getId())
                .toList();
        return placeIds.stream()
                .map(likedPlaceIds::contains)
                .toList();
    }

    public RecResDto fetchRecommendedPlaces(RecReqDto request) {
        try {
            RecResDto response =  restClient.post()
                    .uri(AI_SERVER_URL)
                    .body(request)
                    .retrieve()
                    .body(RecResDto.class);
            logger.info("RESPONSE FROM AI SERVER: {}", response);
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("FAILED TO GET RECOMMENDATION: {}", e.getResponseBodyAsString());
            throw new GoogleApiException("추천 실패: " + e.getMessage());
        }
    }
}
