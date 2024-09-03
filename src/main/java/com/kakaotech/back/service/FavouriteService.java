package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.FavouriteRepository;
import com.kakaotech.back.repository.MemberRepository;
import com.kakaotech.back.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    public List<Favourite> getFavourites(String memberId) {
        return favouriteRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundException(NotFoundException.messageWithInfo(memberId)));
    }

    @Transactional
    public Boolean registerFavourite(String memberId, RegisterFavouriteDto dto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(NotFoundException.messageWithInfo(memberId)));
        Place place = placeRepository.findById(dto.getPlaceId()).orElseThrow(() -> new NotFoundException(NotFoundException.messageWithInfo(dto.getPlaceId())));
        favouriteRepository.save(
                Favourite
                        .builder()
                        .place(place)
                        .member(member)
                        .build()
        );
        return true;
    }

    @Transactional
    public Long deleteFavourite(Long favouriteId) {
        if (!favouriteRepository.existsById(favouriteId)) {
            throw new NotFoundException(NotFoundException.messageWithInfo(favouriteId));
        }
        favouriteRepository.deleteById(favouriteId);
        return favouriteId;
    }
}
