package com.kakaotech.back.service.place;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.place.PlaceListReqDto;
import com.kakaotech.back.dto.place.PlaceListResDto;
import com.kakaotech.back.dto.place.PlaceReqDto;
import com.kakaotech.back.dto.place.PlaceResDto;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final GooglePlaceService googlePlaceService;
    private final PlaceImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PlaceResDto getPlaceImage(PlaceReqDto dto) {
        Long placeId = dto.placeId();
        // S3에 존재하는지 확인
        byte[] imageData = imageService.fetchImage(placeId);
        if (imageData != null) return PlaceResDto.builder().placeId(placeId).image(imageData).build();

        // Google Place API
        String googlePlaceId = getGooglePlaceId(placeId);
        String photoName = getPlacePhotoName(googlePlaceId);

        // 응답받은 이미지 파일을 S3에 업로드 후 반환
        imageData = googlePlaceService.getPlaceImage(photoName);
        imageService.saveImage(placeId, imageData);
        return PlaceResDto.builder().placeId(placeId).image(imageData).build();
    }

    public PlaceListResDto getRecommendedImages(PlaceListReqDto dto) {
        return PlaceListResDto.builder().build();
    }

    private PlaceCoordVO getCoordinate(Long placeId) {
        return placeRepository
                .findCoordById(placeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 장소입니다: " + placeId));
    }

    private String getGooglePlaceId(Long placeId) {
        PlaceCoordVO coord = getCoordinate(placeId);
        Map<String, Object> nearbyPlace = googlePlaceService.getNearbyPlace(coord);
        return googlePlaceService.extractPlaceId(nearbyPlace);
    }

    private String getPlacePhotoName(String googlePlaceId) {
        Map<String, Object> placeDetails = googlePlaceService.getPlaceDetails(googlePlaceId);
        return googlePlaceService.extractPhotoName(placeDetails);
    }
}
