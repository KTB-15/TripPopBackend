package com.kakaotech.back.controller;

import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/favourite")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<Boolean> registerFavourite(@RequestBody RegisterFavouriteDto dto, @AuthenticationPrincipal UserDetails principal) {
        logger.info("USER DETAIL: {}", principal);
        favouriteService.registerFavourite(principal.getUsername(), dto);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{favouriteId}")
    public ResponseEntity<Long> deleteFavourite(@PathVariable Long favouriteId) {
        favouriteService.deleteFavourite(favouriteId);
        return ResponseEntity.ok(favouriteId);
    }
}
