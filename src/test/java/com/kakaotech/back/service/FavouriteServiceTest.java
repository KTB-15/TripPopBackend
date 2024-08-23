package com.kakaotech.back.service;

import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.entity.Favourite;
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
                .build();
        place = Place.builder()
                .id(1L)
                .build();
        favourite = Favourite.builder()
                .member(member)
                .place(place)
                .build();
    }

    @Test
    @DisplayName("즐겨찾기 등록")
    void testRegisterFavourite() {
        // Arrange
        RegisterFavouriteDto dto = RegisterFavouriteDto.builder()
                .memberId(member.getId())
                .placeId(place.getId())
                .build();
        when(memberRepository.findById(dto.getMemberId())).thenReturn(Optional.of(member));
        when(placeRepository.findById(dto.getPlaceId())).thenReturn(Optional.of(place));
        when(favouriteRepository.save(any(Favourite.class))).thenReturn(favourite);

        // Act
        Boolean result = favouriteService.registerFavourite(dto);

        // Assert
        assertEquals(true, result);
    }

    @Test
    @DisplayName("즐겨찾기 등록 이전에 회원 조회 실패")
    void failRegisterFavouriteUserNotFound() {
        // Arrange
        RegisterFavouriteDto dto = RegisterFavouriteDto.builder()
                .memberId("WRONG_ID")
                .build();
        when(memberRepository.findById(dto.getMemberId())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(MemberException.class, () -> favouriteService.registerFavourite(dto));

        verify(memberRepository, times(1)).findById(dto.getMemberId());
        verify(placeRepository, never()).findById(any());
        verify(favouriteRepository, never()).save(any(Favourite.class));
    }

    @Test
    void testDeleteFavourite() {
        // Arrange
        Long favouriteId = 1L;
        when(favouriteRepository.existsById(favouriteId)).thenReturn(true);

        // When
        Long deletedFavouriteId = favouriteService.deleteFavourite(favouriteId);

        // Then
        assertEquals(1L, deletedFavouriteId);
        verify(favouriteRepository, times(1)).existsById(favouriteId);
        verify(favouriteRepository, times(1)).deleteById(favouriteId);
    }
}
