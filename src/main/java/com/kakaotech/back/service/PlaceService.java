package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.entity.projection.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceCoordVO getCoordinate(Long placeId) {
        if (!placeRepository.existsById(placeId)) throw new NotFoundException(placeId+"로 생성된 place는 존재하지 않습니다.");
        return placeRepository.findCoordById(placeId).get();
    }
}
