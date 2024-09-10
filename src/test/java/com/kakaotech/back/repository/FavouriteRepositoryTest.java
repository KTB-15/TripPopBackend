package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.Place;
import jakarta.transaction.Transactional;
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
    @Autowired
    private MemberRepository memberRepository;

    private Favourite favourite1;
    private Place place1;
    private Member member1;

    @BeforeEach
    void setup() {
        place1 = Place.builder()
                .areaName("KOREA")
                .roadName("KOREA")
                .xCoord(126.0)
                .yCoord(36.0)
                .build();
        member1 = Member.builder()
                .memberId("1234")
                .password("1234")
                .gender(Gender.MALE)
                .age(20)
                .build();
        placeRepository.save(place1);
        memberRepository.save(member1);
        favourite1 = Favourite.builder()
                .place(place1)
                .member(member1)
                .registerAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void cleanup() {
        placeRepository.delete(place1);
        memberRepository.delete(member1);
        favouriteRepository.delete(favourite1);
    }

    @Test
    @DisplayName("장소 얻기")
    void testGetPlace() {
        // Arrange + Act
        Favourite favourite = favouriteRepository.save(favourite1);
        // Assert
        Assertions.assertEquals(favourite.getPlace().getAreaName(), place1.getAreaName());
        Assertions.assertEquals(favourite.getMember().getMemberId(), member1.getMemberId());
    }
}
