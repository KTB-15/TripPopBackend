package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Place;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FavouriteRepositoryTest {

    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private PlaceRepository placeRepository;

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
        placeRepository.save(place1);
        favourite1 = Favourite.builder()
                .place(place1)
                .registerAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void cleanup() {
        placeRepository.deleteAll();
        favouriteRepository.deleteAll();
    }

    @Test
    @DisplayName("장소 얻기")
    void testGetPlace() {
        // Arrange
        favouriteRepository.save(favourite1);
        // Act
        Favourite retrievedFavourite = favouriteRepository.findAll().getFirst();
        // Assert
        Assertions.assertEquals(retrievedFavourite.getPlace().getId(), place1.getId());
    }
}
