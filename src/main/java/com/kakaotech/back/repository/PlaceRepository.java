package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Place;
import com.kakaotech.back.vo.PlaceCoordVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<PlaceCoordVO> findCoordById(Long id);
    List<Place> findByIdInOrderById(List<Long> id);
}
