package com.kakaotech.back.repository;

import com.kakaotech.back.entity.Gender;
import com.kakaotech.back.entity.History;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.SGG;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SGGRepository sggRepository;
    @Autowired
    private HistoryRepository historyRepository;
    private Member member;
    private History history;

    @BeforeEach
    public void setUp() {
        SGG sgg = new SGG("sgg1", "sidoCode1", "sggCode1", "sidoName1", "sggName1");
        sggRepository.save(sgg);  // SGG 엔터티를 먼저 저장


        history = History.builder()
                .content("Sample content")
                .build();
        history = historyRepository.save(history);

        member = Member.builder()
                .id("member1")
                .memberId("mem001")
                .password("password")
                .nickname("nickname")
                .gender(Gender.MALE)
                .age(30)
                .histories(List.of(history))
                .sgg(sgg)
                .build();
    }

    @Test
    public void testSaveMember() {
        Member savedMember = memberRepository.save(member);
        assertNotNull(savedMember);
        assertEquals("member1", savedMember.getId());
    }

    @Test
    public void testFindMemberById() {
        memberRepository.save(member);
        Member foundMember = memberRepository.findById("member1").orElse(null);
        assertNotNull(foundMember);
        assertEquals("mem001", foundMember.getMemberId());
    }

    @Test
    public void testRelationshipMemberAndHistory() {
        memberRepository.save(member);
        Member foundMember = memberRepository.findById("member1").orElse(null);
        assertEquals("Sample content", foundMember.getHistories().get(0).getContent());
    }
}
