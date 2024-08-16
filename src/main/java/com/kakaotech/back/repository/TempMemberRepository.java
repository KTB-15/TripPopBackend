package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempMemberRepository extends JpaRepository<Member, String>{
}
