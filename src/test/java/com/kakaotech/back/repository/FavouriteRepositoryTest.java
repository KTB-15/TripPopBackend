package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Place;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavouriteRepositoryTest {

    @Mock
    private FavouriteRepository favouriteRepository;

    private Favourite favourite1;
    private Place place1;

    @BeforeEach
    void setup() {
        place1 = Place.builder()
                .id(1L)
                .areaName("KOREA")
                .roadName("KOREA")
                .xCoord(126.0)
                .yCoord(36.0)
                .build();
        favourite1 = Favourite.builder()
                .place(place1)
                .registerAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void cleanup() {
        favouriteRepository.deleteAll();
    }

    @Test
    @DisplayName("장소 얻기")
    void testGetPlace() {
        // Arrange
        when(favouriteRepository.findAll()).thenReturn(List.of(favourite1));
        // Act
        Favourite retrievedFavourite = favouriteRepository.findAll().getFirst();
        // Assert
        Assertions.assertEquals(retrievedFavourite.getPlace().getId(), place1.getId());
    }
}
