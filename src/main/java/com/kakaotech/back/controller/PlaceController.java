package com.kakaotech.back.controller;

import com.kakaotech.back.dto.place.PlaceReqDto;
import com.kakaotech.back.service.PlaceService;
import com.kakaotech.back.vo.GooglePlaceIdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

//    @GetMapping("/{placeId}")
//    public ResponseEntity<ApiResponse<PlaceCoordVO>> getPlaceCoord(@PathVariable Long placeId) {
//        return ResponseEntity.ok(ApiResponse.success(placeService.getCoordinate(placeId)));
//    }

    @PostMapping("")
    public ResponseEntity<GooglePlaceIdVO> getPlaceInfo(@RequestBody PlaceReqDto dto) {
        return ResponseEntity.ok(placeService.getNearbyPlace(dto.placeId()));
    }

    @PostMapping("/reference")
    public ResponseEntity<Map<String, Object>> getPlaceReference(@RequestBody PlaceReqDto dto) {
        return ResponseEntity.ok(placeService.getPlaceReference(dto.placeId()));
    }
}
