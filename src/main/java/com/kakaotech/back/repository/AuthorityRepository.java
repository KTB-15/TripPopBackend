package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    boolean existsByAuthorityName(String authorityName);
}

