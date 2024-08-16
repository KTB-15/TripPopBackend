package com.kakaotech.back.repository;

import com.kakaotech.back.entity.SGG;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SGGRepositoryTest {

    @Autowired
    private SGGRepository sggRepository;
    private SGG sgg;

    @BeforeEach
    public void setUp() {
        sgg = SGG.builder()
                .id("sgg1")
                .sidoCode("sidoCode1")
                .sggCode("sggCode1")
                .sidoName("sidoName1")
                .sggName("sggName1")
                .build();
    }

    @Test
    public void testSaveSGG() {
        SGG savedSGG = sggRepository.save(sgg);
        assertNotNull(savedSGG);
        assertEquals("sgg1", savedSGG.getId());
    }

    @Test
    public void testFindSGGById() {
        sggRepository.save(sgg);
        SGG foundSGG = sggRepository.findById("sgg1").orElse(null);
        assertNotNull(foundSGG);
        assertEquals("sidoCode1", foundSGG.getSidoCode());
    }
}
