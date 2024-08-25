package com.kakaotech.back.controller;

import com.kakaotech.back.dto.place.PlaceReqDto;
import com.kakaotech.back.service.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    // TODO: S3 URL 응답
    @PostMapping(value = "/image", produces = "image/*")
    public ResponseEntity<byte[]> getPlaceImage(@RequestBody PlaceReqDto dto) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(placeService.getPlaceImage(dto.placeId()));
    }
}
