package com.kakaotech.back.service;

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
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<Favourite> getFavourites(String memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) throw new MemberException(ErrorMessage.USER_NOT_FOUND);

        return favouriteRepository.findByMember(member.get()).get();
    }

    @Transactional
    public Boolean registerFavourite(RegisterFavouriteDto dto) {
        Optional<Member> member = memberRepository.findById(dto.getMemberId());
        // Member 찾기
        if (member.isEmpty()) {
            throw new MemberException(ErrorMessage.USER_NOT_FOUND);
        }
        Optional<Place> place = placeRepository.findById(dto.getPlaceId());
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
    public Long deleteFavourite(Long favouriteId) {
        if (!favouriteRepository.existsById(favouriteId)) {
            throw new FavouriteException(ErrorMessage.FAVOURITE_NOT_FOUND);
        }
        favouriteRepository.deleteById(favouriteId);
        return favouriteId;
    }
}
