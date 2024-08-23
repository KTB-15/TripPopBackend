package com.kakaotech.back.service;

import com.kakaotech.back.entity.projection.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceCoordVO getCoordinate(Long placeId) {
        if (!placeRepository.existsById(placeId)) throw new PlaceException(ErrorMessage.PLACE_NOT_FOUND);
        return placeRepository.findCoordById(placeId).get();
    }
}
