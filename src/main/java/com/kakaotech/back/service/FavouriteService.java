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
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<Favourite> getFavourites(String memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) throw new NotFoundException(memberId+"로 생성된 member는 존재하지 않습니다.");

        return favouriteRepository.findByMember(member.get()).get();
    }

    @Transactional
    public Boolean registerFavourite(RegisterFavouriteDto dto) {
        Optional<Member> member = memberRepository.findById(dto.getMemberId());
        // Member 찾기
        if (member.isEmpty()) {
            throw new NotFoundException(dto.getMemberId()+"로 생성된 member는 존재하지 않습니다.");
        }
        Optional<Place> place = placeRepository.findById(dto.getPlaceId());
        // Place 찾기
        if (place.isEmpty()) {
            throw new NotFoundException(dto.getPlaceId()+"로 생성된 place는 존재하지 않습니다.");
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
            throw new NotFoundException(favouriteId+"로 생성된 favourite는 존재하지 않습니다.");
        }
        favouriteRepository.deleteById(favouriteId);
        return favouriteId;
    }
}
