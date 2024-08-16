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

    //    TODO: Merge시 원본 Member로 변경해야함
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

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
    private Visit(Place place, Member member, int residenceTime, int visitTypeCode, String revisitYN, int rating, int revisitIntention) {
        this.place = place;
        this.member = member;
        this.residenceTime = residenceTime;
        this.visitTypeCode = visitTypeCode;
        this.revisitYN = revisitYN;
        this.rating = rating;
        this.revisitIntention = revisitIntention;
    }
}
