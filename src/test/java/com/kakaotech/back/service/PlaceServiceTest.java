package com.kakaotech.back.service;

import com.kakaotech.back.service.place.PlaceService;
import com.kakaotech.back.vo.GooglePlaceIdVO;
import com.kakaotech.back.vo.PlaceCoordVO;
import com.kakaotech.back.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.*;

import java.util.List;
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
        MockitoAnnotations.openMocks(this); // mock 생성을 위한 초기화
    }

    @Test
    @DisplayName("Google Places API에 좌표를 통해 Place 정보 얻기")
    void testGetNearbyPlace() {
        // Arrange
        // RestClient 관련 mock
        RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class); // HTTP 메서드 지정 인터페이스
        RequestBodySpec requestBodySpec = mock(RequestBodySpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class); // 요청 실행 및 응답 준비. body로 타입 매핑, onStatus로 예외 처리.

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq("X-Goog-FieldMask"), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq("X-Goog-Api-Key"), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(PlaceCoordVO.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        GooglePlaceIdVO expected = GooglePlaceIdVO.builder().places(List.of(new GooglePlaceIdVO.Place("place"))).build();
        when(responseSpec.body(eq(GooglePlaceIdVO.class))).thenReturn(expected);

        PlaceCoordVO coord = mock(PlaceCoordVO.class);
        when(coord.getxCoord()).thenReturn(126.9238966);
        when(coord.getyCoord()).thenReturn(33.45231691);
        when(placeRepository.findCoordById(1L)).thenReturn(Optional.of(coord));

        // Act
        GooglePlaceIdVO result = placeService.getNearbyPlace(1L);

        // Assert
        assertNotNull(result);
        assertEquals(result.places(), expected.places());
    }

}