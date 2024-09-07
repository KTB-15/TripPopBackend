package com.kakaotech.back.controller;

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
    public ResponseEntity<Boolean> registerFavourite(@RequestBody RegisterFavouriteDto dto) {
        favouriteService.registerFavourite(dto);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{favouriteId}")
    public ResponseEntity<Long> deleteFavourite(@PathVariable Long favouriteId) {
        favouriteService.deleteFavourite(favouriteId);
        return ResponseEntity.ok(favouriteId);
    }
}
