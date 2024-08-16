package com.kakaotech.back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    TODO: Merge시 원본 Member로 변경해야함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private TempMember member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String content;

    @CreatedDate
    @Column(name = "register_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime registerAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Builder
    public Review(TempMember member, Place place, String content, LocalDateTime registerAt, LocalDateTime updatedAt) {
        this.member = member;
        this.place = place;
        this.content = content;
        this.registerAt = registerAt;
        this.updatedAt = updatedAt;
    }
}
