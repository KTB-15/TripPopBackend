package com.kakaotech.back.service.place;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.place.PlaceListReqDto;
import com.kakaotech.back.dto.place.PlaceListResDto;
import com.kakaotech.back.dto.place.PlaceReqDto;
import com.kakaotech.back.dto.place.PlaceResDto;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final GooglePlaceService googlePlaceService;
    private final PlaceImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Place> getPlacesByAreaNames(List<String> areaNames){
        return placeRepository.findByAreaNameInOrderById(areaNames);
    }

    public PlaceResDto getPlaceDto(Long placeId) {
        byte[] image = getPlaceImage(placeId);
        imageService.saveImage(placeId, image);
        return PlaceResDto.builder().placeId(placeId).image(image).build();
    }

    public byte[] getPlaceImage(Long placeId){
        // S3에 존재하는지 확인
        byte[] imageData = imageService.fetchImage(placeId);
        if (imageData != null) return imageData;

        String googlePlaceId = getGooglePlaceId(placeId);
        String photoName = getPlacePhotoName(googlePlaceId);

        imageData = googlePlaceService.getPlaceImage(photoName);
        imageService.saveImage(placeId, imageData);
        return imageData;
    }

    public Place getPlace(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 장소입니다: " + placeId)
        );
    }

    public List<Place> getPlacesInOrder(List<Long> placeIds){
        return placeRepository.findByIdInOrderById(placeIds);
    }

    public PlaceListResDto getRecommendedImages(PlaceListReqDto dto) {
        List<PlaceResDto> recommended = dto.places().parallelStream().map(this::getPlaceDto).toList();
        return PlaceListResDto.builder().places(recommended).build();
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
