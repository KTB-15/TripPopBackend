package com.kakaotech.back.controller;

import com.kakaotech.back.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

//    @GetMapping("/{placeId}")
//    public ResponseEntity<ApiResponse<PlaceCoordVO>> getPlaceCoord(@PathVariable Long placeId) {
//        return ResponseEntity.ok(ApiResponse.success(placeService.getCoordinate(placeId)));
//    }

    @PostMapping("/{placeId}")
    public ResponseEntity<String> getPlaceInfo(@PathVariable Long placeId) {
        return ResponseEntity.ok(placeService.getNearbyPlace(placeId));
    }
}
