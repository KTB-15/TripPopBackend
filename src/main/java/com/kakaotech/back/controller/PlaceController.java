package com.kakaotech.back.controller;

import com.kakaotech.back.dto.place.PlaceListReqDto;
import com.kakaotech.back.dto.place.PlaceListResDto;
import com.kakaotech.back.dto.place.PlaceReqDto;
import com.kakaotech.back.dto.place.PlaceResDto;
import com.kakaotech.back.service.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping("/image")
    public ResponseEntity<PlaceResDto> getPlaceImage(@RequestBody PlaceReqDto dto) {
        return ResponseEntity.ok(placeService.getPlaceImage(dto.placeId()));
    }

    @PostMapping("/recommended")
    public ResponseEntity<PlaceListResDto> getRecommendedImages(@RequestBody PlaceListReqDto dto){
        return ResponseEntity.ok(placeService.getRecommendedImages(dto));
    }
}
