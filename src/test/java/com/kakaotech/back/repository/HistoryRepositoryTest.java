package com.kakaotech.back.repository;

import com.kakaotech.back.entity.History;
import com.kakaotech.back.entity.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HistoryRepositoryTest {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private PlaceRepository placeRepository;
    private History history;
    private Place place;

    @BeforeEach
    public void setUp() {
        place = Place.builder()
                .id("Sample Place")
                .areaName("Sample Area")
                .roadName("Sample Road")
                .xCoord(123.45)
                .yCoord(67.89)
                .imageUrl("http://example.com/image.jpg")
                .build();

        place = placeRepository.save(place);

        assertNotNull(place.getId()); // null test

        history = History.builder()
                .content("Sample content")
                .place(place)
                .build();
    }

    @Test
    public void testSaveHistory() {
        History savedHistory = historyRepository.save(history);
        assertNotNull(savedHistory);
        assertEquals("Sample content", savedHistory.getContent());
        assertNotNull(savedHistory.getPlace());
        assertEquals(place.getId(), savedHistory.getPlace().getId());
    }

    @Test
    public void testFindHistoryById() {
        History savedHistory = historyRepository.save(history);
        History foundHistory = historyRepository.findById(savedHistory.getId()).orElse(null);
        assertNotNull(foundHistory);
        assertEquals("Sample content", foundHistory.getContent());
        assertNotNull(foundHistory.getPlace());
        assertEquals(place.getId(), foundHistory.getPlace().getId());
    }
}
