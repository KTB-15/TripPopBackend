package com.kakaotech.back.service;

import com.kakaotech.back.entity.projection.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.*;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaceServiceTest {
    @InjectMocks
    private PlaceService placeService;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // RestClient 관련 mock
        RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class);
        RequestBodySpec requestBodySpec = mock(RequestBodySpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Map.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        String expectedResponse = "{TODO}";
        when(responseSpec.body(eq(String.class))).thenReturn(expectedResponse);
    }

    @Test
    @DisplayName("Google Places API에 좌표를 통해 Place 정보 얻기")
    void testGetNearbyPlace() {
        // Arrange
        PlaceCoordVO coord = mock(PlaceCoordVO.class);
        when(coord.getxCoord()).thenReturn(126.9238966);
        when(coord.getyCoord()).thenReturn(33.45231691);
        when(placeRepository.findCoordById(1L)).thenReturn(Optional.of(coord));

        // Act
        String response = placeService.getNearbyPlace(coord);

        // Assert
        assertNotNull(response);
        assertEquals("{TODO}", response);
    }
}