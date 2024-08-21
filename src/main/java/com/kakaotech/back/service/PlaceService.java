package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.PlaceException;
import com.kakaotech.back.entity.segment.PlaceCoord;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceCoord getCoordinate(Long placeId) {
        if (!placeRepository.existsById(placeId)) throw new PlaceException(ErrorMessage.PLACE_NOT_FOUND);
        return placeRepository.findCoordById(placeId).get();
    }
}
