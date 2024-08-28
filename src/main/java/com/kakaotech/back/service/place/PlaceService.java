package com.kakaotech.back.service.place;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final GooglePlaceService googlePlaceService;
    private final PlaceImageService imageService;

    public byte[] getPlaceImage(Long placeId) {
        String googlePlaceId = getGooglePlaceId(placeId);
        String photoName = getPlacePhotoName(googlePlaceId);

        byte[] imageData = googlePlaceService.getPlaceImage(photoName);
        imageService.saveImage(placeId, imageData);

        return imageData;
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
