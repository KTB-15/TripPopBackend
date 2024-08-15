package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Favourite;
import com.kakaotech.back.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
}
