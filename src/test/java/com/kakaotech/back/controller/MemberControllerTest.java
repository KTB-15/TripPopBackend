package com.kakaotech.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotech.back.dto.member.MemberSurveyDto;
import com.kakaotech.back.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MemberController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("여행 성향 변경")
    void testPatchTravelStyle() throws Exception {
        // Given
        String memberId = "testId";
        MemberSurveyDto request = MemberSurveyDto.builder()
                .travelStyle1(1)
                .travelStyle2(2)
                .build();
        MemberSurveyDto response = MemberSurveyDto.builder()
                .travelStyle1(3)
                .travelStyle2(4)
                .build();

        // When
        when(memberService.updateSurvey(eq(memberId), eq(request))).thenReturn(response);

        // Then
        mockMvc.perform(patch("/member/survey/{memberId}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.travelStyle1").value(response.travelStyle1()))
                .andExpect(jsonPath("$.travelStyle2").value(response.travelStyle2()));
    }
}
