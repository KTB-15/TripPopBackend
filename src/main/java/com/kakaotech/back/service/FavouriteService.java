package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.FavouriteException;
import com.kakaotech.back.common.exception.MemberException;
import com.kakaotech.back.common.exception.PlaceException;
import com.kakaotech.back.dto.favourite.DeleteFavouriteDto;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.entity.TempMember;
import com.kakaotech.back.repository.FavouriteRepository;
import com.kakaotech.back.repository.PlaceRepository;
import com.kakaotech.back.repository.TempMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final TempMemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    @Autowired
    public FavouriteService(FavouriteRepository favouriteRepository, TempMemberRepository memberRepository, PlaceRepository placeRepository) {
        this.favouriteRepository = favouriteRepository;
        this.memberRepository = memberRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public Boolean registerFavourite(RegisterFavouriteDto dto) {
        Optional<TempMember> member = memberRepository.findById(dto.getMemberId());
        Optional<Place> place = placeRepository.findById(dto.getPlaceId());
        // Member 찾기
        if (member.isEmpty()) {
            throw new MemberException(ErrorMessage.USER_NOT_FOUND);
        }
        // Place 찾기
        if (place.isEmpty()) {
            throw new PlaceException(ErrorMessage.PLACE_NOT_FOUND);
        }
        favouriteRepository.save(
                Favourite
                        .builder()
                        .place(place.get())
                        .member(member.get())
                        .build()
        );
        return true;
    }

    @Transactional
    public Long deleteFavourite(DeleteFavouriteDto dto) {
        if (!favouriteRepository.existsById(dto.getFavouriteId())) {
            throw new FavouriteException(ErrorMessage.FAVOURITE_NOT_FOUND);
        }
        favouriteRepository.deleteById(dto.getFavouriteId());
        return dto.getFavouriteId();
    }
}
