package com.kakaotech.back.repository;

import com.kakaotech.back.common.exception.ErrorMessage;
import com.kakaotech.back.common.exception.PlaceException;
import com.kakaotech.back.entity.Place;
import com.kakaotech.back.entity.projection.PlaceCoordVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;
    private Place place;
    private Long placeId;

    private final Double xCoord = 123.123456;
    private final Double yCoord = 36.123456;

    @BeforeEach
    public void setUp() {
        place = Place.builder()
                .areaName("Korea")
                .roadName("Anywhere")
                .xCoord(xCoord)
                .yCoord(yCoord)
                .build();

        // Given
        placeRepository.save(place);
        placeId = place.getId();
    }

    @Test
    @DisplayName("Projection으로 좌표 구하기")
    void testPlaceProjection() {
        // When
        assertNotNull(placeId);
        PlaceCoordVO vo = placeRepository.findCoordById(placeId).orElseThrow(
                () -> new PlaceException(ErrorMessage.PLACE_NOT_FOUND)
        );

        // Then
        assertEquals(xCoord, vo.getxCoord());
        assertEquals(yCoord, vo.getyCoord());
    }
}
