package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Place;
import com.kakaotech.back.entity.segment.PlaceCoord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
    Optional<PlaceCoord> findCoordById(String id);
}
