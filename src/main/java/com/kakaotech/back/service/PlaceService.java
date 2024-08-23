package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.PlaceException;
import com.kakaotech.back.vo.GooglePlaceIdVO;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final RestClient restClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String NEARBY_SEARCH_URL = "https://places.googleapis.com/v1/places:searchNearby"; // 좌표의 Place Id 조회
    private final String PLACE_REFERENCE_URL = "https://places.googleapis.com/v1/places"; // Place Id에 대한 정보 조회

    // 좌표를 통해 구글 Place 주변 장소 검색
    public GooglePlaceIdVO getNearbyPlace(Long placeId) {
        // 존재하는 id인지 확인
        PlaceCoordVO coord = getCoordinate(placeId);
        var body = getNearbyReqBody(coord);
        logger.info(body.toString());
        try {
            return restClient.post()
                    .uri(NEARBY_SEARCH_URL)
                    .body(body)
                    .retrieve()
                    .body(GooglePlaceIdVO.class);
        } catch (HttpClientErrorException e) {
            logError(e);
            throw new PlaceException(ErrorMessage.GET_PLACE_IMAGE_FAILED);
        }
    }

    public Map<String, Object> getPlaceReference(Long placeId) {
        List<GooglePlaceIdVO.Place> googlePlaces = getNearbyPlace(placeId).places();
        // 좌표에 대한 장소가 없는 경우
        if (googlePlaces.isEmpty()) throw new PlaceException(ErrorMessage.GET_PLACE_IMAGE_FAILED);
        String idUrl = googlePlaces.getFirst().name();
        String googlePlaceId = idUrl.substring(idUrl.lastIndexOf('/') + 1);


        try {
            return restClient.get()
                    .uri(new StringBuilder(PLACE_REFERENCE_URL).append(googlePlaceId).toString())
                    .retrieve()
                    .body(Map.class);
        } catch (HttpClientErrorException e) {
            logError(e);
            throw new PlaceException(ErrorMessage.GET_PLACE_IMAGE_FAILED);
        }
    }

    // API 실패 로그 출력
    private void logError(HttpClientErrorException e) {
        logger.error("FAIL TO GET GOOGLE PLACE INFO: {} - {}",
                e.getStatusCode().value(),
                e.getResponseBodyAsString());
    }

    // 좌표만 조회
    private PlaceCoordVO getCoordinate(Long placeId) {
        return placeRepository
                .findCoordById(placeId)
                .orElseThrow(() -> new PlaceException(ErrorMessage.PLACE_NOT_FOUND));
    }

    // Google Places nearby api 요청 데이터
    private static Map<String, Object> getNearbyReqBody(PlaceCoordVO coord) {
        Double longitude = coord.getxCoord();
        Double latitude = coord.getyCoord();
        return Map.of(
                "maxResultCount", 3, // 최대 3개를
                "rankPreference", "DISTANCE", // 가까운 거리순으로
                "locationRestriction", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", latitude,
                                        "longitude", longitude
                                ),
                                "radius", 100 // 반경 100m
                        )
                )
        );
    }
}
