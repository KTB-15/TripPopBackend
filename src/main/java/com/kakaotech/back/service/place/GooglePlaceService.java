package com.kakaotech.back.service.place;

import com.kakaotech.back.common.exception.GoogleApiException;
import com.kakaotech.back.vo.PlaceCoordVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GooglePlaceService {
    private final RestClient restClient;

    private static final String GOOGLE_API_URL_PREFIX = "https://places.googleapis.com/v1/";
    private static final String NEARBY_SEARCH_URL = GOOGLE_API_URL_PREFIX + "places:searchNearby";
    private static final String PLACE_REFERENCE_URL = GOOGLE_API_URL_PREFIX + "places";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${google.maps.api-key}")
    private String apiKey;

    public Map getNearbyPlace(PlaceCoordVO coord) {
        var body = getNearbyReqBody(coord);
        try {
            return restClient.post()
                    .uri(NEARBY_SEARCH_URL)
                    .header("X-Goog-FieldMask", "places.name")
                    .header("X-Goog-Api-Key", apiKey)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .body(Map.class);
        } catch (HttpClientErrorException e) {
            logger.error("FAILED TO GET PLACE ID: {}", e.getResponseBodyAsString());
            throw new GoogleApiException("GOOGLE PLACE 조회 실패: " + e.getMessage());
        }
    }

    public Map<String, Object> getPlaceDetails(String googlePlaceId) {
        try {
            return (Map<String, Object>) restClient.get()
                    .uri(PLACE_REFERENCE_URL + "/" + googlePlaceId)
                    .header("X-Goog-FieldMask", "photos,name,websiteUri,reviews,googleMapsUri")
                    .header("X-Goog-Api-Key", apiKey)
                    .retrieve()
                    .body(Map.class);
        } catch (HttpClientErrorException e) {
            logger.error("FAILED TO GET PLACE REFERENCE: {}", e.getResponseBodyAsString());
            throw new GoogleApiException("GOOGLE PLACE IMAGE 경로 획득 실패");
        }
    }

    public byte[] getPlaceImage(String photoName) {
        String url = GOOGLE_API_URL_PREFIX + photoName + "/media?maxHeightPx=600&maxWidthPx=600&key=" + apiKey;
        try {
            byte[] image = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(byte[].class);
            if (image == null) throw new GoogleApiException("GOOGLE IMAGE 비정상 응답");
            return image;
        } catch (HttpClientErrorException e) {
            logger.error("FAILED TO GET IMAGE: {}", e.getResponseBodyAsString());
            throw new GoogleApiException("GOOGLE IMAGE 획득 실패");
        }
    }

    public String extractPhotoName(Map<String, Object> placeDetails) {
        logger.info("PLACE REFERENCE: {}", placeDetails);
        return ((Map<String, Object>) ((List<Map<String, Object>>) placeDetails.get("photos")).getFirst()).get("name").toString();
    }

    public String extractPlaceId(Map<String, Object> placeDetails) {
        logger.info("PLACE INFO: {}", placeDetails);
        String origin = ((Map<String, Object>) ((List<Map<String, Object>>) placeDetails.get("places")).getFirst()).get("name").toString();
        return origin.split("/")[1];
    }

    private static Map<String, Object> getNearbyReqBody(PlaceCoordVO coord) {
        return Map.of(
                "maxResultCount", 3, // 최대 3개 장소
                "rankPreference", "POPULARITY", // 거리순
                "locationRestriction", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", coord.getyCoord(),
                                        "longitude", coord.getxCoord()
                                ),
                                "radius", 300 // 100m 이내
                        )
                )
        );
    }
}
