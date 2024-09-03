package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    Optional<List<Favourite>> findByMemberId(String memberId);
    Optional<List<Favourite>> findByMemberIdAndPlaceIdIn(String memberId, List<Long> placeIds);
}
