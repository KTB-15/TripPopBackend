package com.kakaotech.back.controller;

import com.kakaotech.back.common.api.ApiResponse;
import com.kakaotech.back.dto.favourite.DeleteFavouriteDto;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.service.FavouriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavouriteController {
    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> registerFavourite(RegisterFavouriteDto dto) {
        favouriteService.registerFavourite(dto);
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Long>> deleteFavourite(DeleteFavouriteDto dto) {
        favouriteService.deleteFavourite(dto);
        return ResponseEntity.ok(ApiResponse.success(dto.getFavouriteId()));
    }
}
