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

    public Map getNearbyPlace(Long placeId) {
        PlaceCoordVO coord = getCoordinate(placeId);
        return googlePlaceService.getNearbyPlace(coord);
    }

    public Map getPlaceReference(Long placeId) {
        Map nearbyPlace = getNearbyPlace(placeId);
        String googlePlaceId = googlePlaceService.extractPlaceId(nearbyPlace);

        Map placeDetails = googlePlaceService.getPlaceDetails(googlePlaceId);
        String photoName = googlePlaceService.extractPhotoName(placeDetails);

        byte[] imageData = googlePlaceService.getPlaceImage(photoName);
        imageService.saveImage(placeId, imageData);

        return placeDetails;
    }

    private PlaceCoordVO getCoordinate(Long placeId) {
        return placeRepository
                .findCoordById(placeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 장소입니다: " + placeId));
    }
}
