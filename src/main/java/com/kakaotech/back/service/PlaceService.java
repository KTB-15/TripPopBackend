package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.GoogleApiException;
import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import com.kakaotech.back.vo.GooglePlaceIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final RestClient restClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GOOGLE_API_URL_PREFIX = "https://places.googleapis.com/v1/";
    private static final String NEARBY_SEARCH_URL = GOOGLE_API_URL_PREFIX + "places:searchNearby"; // 좌표의 Place Id 조회
    private static final String PLACE_REFERENCE_URL = GOOGLE_API_URL_PREFIX + "places"; // Place Id에 대한 정보 조회

    @Value("${google.maps.api-key}")
    private String apiKey;

    // 좌표를 통해 구글 Place 주변 장소 검색
    public GooglePlaceIdVO getNearbyPlace(Long placeId) {
        // 존재하는 id인지 확인
        PlaceCoordVO coord = getCoordinate(placeId);
        var body = getNearbyReqBody(coord);
        try {
            return restClient.post()
                    .uri(NEARBY_SEARCH_URL)
                    .header("X-Goog-FieldMask", "places.name")
                    .header("X-Goog-Api-Key", apiKey)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .body(GooglePlaceIdVO.class);
        } catch (HttpClientErrorException e) {
            logError(e);
            throw new NotFoundException();
        }
    }

    public Map getPlaceReference(Long placeId) {
        List<GooglePlaceIdVO.Place> googlePlaces = getNearbyPlace(placeId).places();
        logger.info(googlePlaces.toString());
        // 좌표에 대한 장소가 없는 경우
        if (googlePlaces.isEmpty()) throw new GoogleApiException("존재하지 않는 장소입니다: " + placeId);
        String idUrl = googlePlaces.getFirst().name();
        String googlePlaceId = idUrl.substring(idUrl.lastIndexOf('/') + 1);
        String photoName = "";
        try {
            Map photos = restClient.get()
                    .uri(PLACE_REFERENCE_URL + "/" + googlePlaceId)
                    .header("X-Goog-FieldMask", "photos")
                    .header("X-Goog-Api-Key", apiKey)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .body(Map.class);
            photoName = getPhotoName(photos);
            String url = GOOGLE_API_URL_PREFIX + photoName + "/media?maxHeightPx=400&maxWidthPx=400&key=" + apiKey;
            Path path = Paths.get("src", "main", "resources", "image", placeId + ".jpg");
            ResponseEntity<byte[]> response = restClient.get()
                    .uri(url)
                    .header("Accept", "image/*")
                    .retrieve()
                    .toEntity(byte[].class);
            logger.info("Response Header: {}", response.getHeaders());
            logger.info("body: {}", response.getBody());
            logger.info(Arrays.toString(response.getBody())); // JSON 형식 {"name": "places/...", "photoUri": "https://..."}
            // 왜 json 형식으로 응답받는거지?
            Files.write(path, response.getBody()); // 파일 손상됨. json을 jpg로 지정했으니까.
            logger.info("Saved to: {}", path.toAbsolutePath());
            return photos;
        } catch (HttpClientErrorException e) {
            logError(e);
            throw new GoogleApiException("장소 정보 획득에 실패했습니다: " + googlePlaceId);
        } catch (IOException e) {
            throw new GoogleApiException("이미지 획득에 실패했습니다: " + photoName);
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
                .orElseThrow(() -> new NotFoundException("존재하지 않는 장소입니다: " + placeId));
    }

    private String getPhotoName(Map placeReference) {
        return ((Map<String, Object>) ((List<Map<String, Object>>) placeReference.get("photos")).get(0)).get("name").toString();
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
