package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.NotFoundException;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public void registerFavourite(String memberId, Long placeId) {
        Member member = memberRepository.findOneWithAuthoritiesByMemberId(memberId).orElseThrow(() -> new NotFoundException(NotFoundException.messageWithInfo(memberId)));
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new NotFoundException(NotFoundException.messageWithInfo(placeId)));
        favouriteRepository.save(
                Favourite
                        .builder()
                        .place(place)
                        .member(member)
                        .build()
        );
    }

    @Transactional
    public void deleteFavourite(String memberId, Long placeId) {
        favouriteRepository.deleteByMemberIdAndPlaceId(memberId, placeId);
    }
}
