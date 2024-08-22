package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.PlaceException;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final RestClient restClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String NEARBY_SEARCH_URL = "https://places.googleapis.com/v1/places:searchNearby"; // Google Places API URL

    // 좌표를 통해 구글 Place 주변 장소 검색
    public String getNearbyPlace(Long placeId) {
        // 존재하는 ID인지 확인
        PlaceCoordVO coord = getCoordinate(placeId);
        var body = getBody(coord);

        try {
            return restClient.post()
                    .uri(NEARBY_SEARCH_URL)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            logger.error("FAIL TO GET GOOGLE PLACE INFO: {} - {}",
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString());
            throw e;
        }
    }

    // 좌표만 조회
    private PlaceCoordVO getCoordinate(Long placeId) {
        return placeRepository
                .findCoordById(placeId)
                .orElseThrow(() -> new PlaceException(ErrorMessage.PLACE_NOT_FOUND));
    }

    // Google Places nearby api 요청 데이터
    private static Map<String, Object> getBody(PlaceCoordVO coord) {
        Double longitude = coord.getxCoord();
        Double latitude = coord.getyCoord();
        return Map.of(
                "maxResultCount", 3,
                "rankPreference", "DISTANCE",
                "locationRestriction", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", latitude,
                                        "longitude", longitude
                                ),
                                "radius", 100
                        )
                )
        );
    }
}
