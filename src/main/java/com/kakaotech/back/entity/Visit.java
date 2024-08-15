package com.kakaotech.back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

//    TODO: Member 엔티티 생성시 활성화
//    @ManyToOne
//    @JoinColumn(name = "id")
//    private Member member;

    @Column(name = "residence_time")
    private int residenceTime;
    @Column(name = "visit_type_code")
    private int visitTypeCode;
    @Column(name = "revisit_yn")
    private String revisitYN;
    @Column(name = "rating")
    private int rating;
    @Column(name = "revisit_intention")
    private int revisitIntention;

    @Builder
    private Visit(Place place, int residenceTime, int visitTypeCode, String revisitYN, int rating, int revisitIntention) {
        this.place = place;
        this.residenceTime = residenceTime;
        this.visitTypeCode = visitTypeCode;
        this.revisitYN = revisitYN;
        this.rating = rating;
        this.revisitIntention = revisitIntention;
    }
}
