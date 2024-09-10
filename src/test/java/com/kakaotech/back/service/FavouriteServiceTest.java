package com.kakaotech.back.service;

import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.FavouriteRepository;
import com.kakaotech.back.repository.MemberRepository;
import com.kakaotech.back.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class FavouriteServiceTest {
    @Mock
    private FavouriteRepository favouriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private FavouriteService favouriteService;

    private Member member;
    private Place place;
    private Favourite favourite;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id("MEMBERID")
                .memberId("MEMBERID")
                .password("PASSWORD")
                .gender(Gender.MALE)
                .age(20)
                .build();
        place = Place.builder()
                .id(1L)
                .build();
        favourite = Favourite.builder()
                .member(member)
                .place(place)
                .build();
    }
}
