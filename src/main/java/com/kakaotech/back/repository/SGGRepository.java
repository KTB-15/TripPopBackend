package com.kakaotech.back.repository;

import com.kakaotech.back.entity.SGG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SGGRepository extends JpaRepository<SGG, String> {
}
