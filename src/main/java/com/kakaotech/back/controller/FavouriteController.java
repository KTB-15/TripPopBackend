package com.kakaotech.back.controller;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.common.exception.UnauthorizedException;
import com.kakaotech.back.dto.favourite.FavouriteReqDto;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.service.FavouriteService;
import com.kakaotech.back.util.SecurityUtil;
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

    @PostMapping("/toggle")
    public ResponseEntity<Boolean> toggleFavourite(@RequestBody FavouriteReqDto dto) {
        String memberId = SecurityUtil.getCurrentUsername().orElseThrow(() -> new UnauthorizedException("Unauthorized user"));

        if (dto.isFavourite()) favouriteService.deleteFavourite(memberId, dto.placeId());
        else favouriteService.registerFavourite(memberId, dto.placeId());
        return ResponseEntity.ok(true);
    }
}
