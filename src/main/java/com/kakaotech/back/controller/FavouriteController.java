package com.kakaotech.back.controller;

import com.kakaotech.back.common.api.ApiResponse;
import com.kakaotech.back.dto.favourite.DeleteFavouriteDto;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/favourite")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> registerFavourite(@RequestBody RegisterFavouriteDto dto) {
        favouriteService.registerFavourite(dto);
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Long>> deleteFavourite(DeleteFavouriteDto dto) {
        favouriteService.deleteFavourite(dto);
        return ResponseEntity.ok(ApiResponse.success(dto.getFavouriteId()));
    }
}
