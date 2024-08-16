package com.kakaotech.back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    //    TODO: Merge시 원본 Member로 변경해야함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private TempMember member;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime registerAt;

    @Builder
    public Favourite(Place place, LocalDateTime registerAt, TempMember member) {
        this.place = place;
        this.registerAt = registerAt;
        this.member = member;
    }
}
