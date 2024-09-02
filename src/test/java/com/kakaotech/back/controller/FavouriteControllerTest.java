package com.kakaotech.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotech.back.common.exception.NotFoundException;
import com.kakaotech.back.dto.favourite.RegisterFavouriteDto;
import com.kakaotech.back.service.FavouriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = FavouriteController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class FavouriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavouriteService favouriteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("즐겨찾기 등록 성공")
    void testRegisterFavouriteSuccess() throws Exception {
        // Arrange
        String memberId = "MEMBER_ID";
        RegisterFavouriteDto dto = RegisterFavouriteDto.builder().memberId(memberId).placeId(1L).build();
        when(favouriteService.registerFavourite(memberId, dto)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(post("/favourite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("즐겨찾기 등록 실패 - 404")
    @WithMockUser(username = "MEMBER_ID", roles = "USER")
    void testRegisterFavouriteFailNotFound() throws Exception {
        // Arrange
        RegisterFavouriteDto dto = RegisterFavouriteDto.builder().memberId("MEMBER_ID").placeId(1L).build();
        when(favouriteService.registerFavourite(anyString(), any())).thenThrow(new NotFoundException("존재하지 않은 member 입니다."));

        // Act + Assert
        mockMvc.perform(post("/favourite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("즐겨찾기 삭제 성공")
    void testDeleteFavouriteSuccess() throws Exception {
        // Arrange
        Long favouriteId = 1L;
        when(favouriteService.deleteFavourite(favouriteId)).thenReturn(favouriteId);

        // Act + Assert
        mockMvc.perform(delete("/favourite/{favouriteId}", favouriteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(favouriteId));
    }

    @Test
    @DisplayName("즐겨찾기 삭제 실패 - 404")
    void testDeleteFavouriteFailNotFound() throws Exception {
        // Arrange
        Long favouriteId = -1L;
        doThrow(new NotFoundException(favouriteId + "로 생성된 favourite는 존재하지 않습니다.")).when(favouriteService).deleteFavourite(favouriteId);

        // Act + Assert
        mockMvc.perform(delete("/favourite/{favouriteId}", favouriteId))
                .andExpect(status().isNotFound());
    }
}
