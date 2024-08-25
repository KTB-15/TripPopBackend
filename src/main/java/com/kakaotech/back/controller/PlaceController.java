package com.kakaotech.back.controller;

import com.kakaotech.back.dto.place.PlaceReqDto;
import com.kakaotech.back.service.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping("/reference")
    public ResponseEntity<Map<String, Object>> getPlaceReference(@RequestBody PlaceReqDto dto) {
        return ResponseEntity.ok(placeService.getPlaceReference(dto.placeId()));
    }
}
