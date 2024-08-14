package com.kakaotech.back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private String content;

    @CreatedDate
    @Column(name = "register_at")
    private LocalDateTime registerAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Review(Place place, String content, LocalDateTime registerAt, LocalDateTime updatedAt) {
        this.place = place;
        this.content = content;
        this.registerAt = registerAt;
        this.updatedAt = updatedAt;
    }
}
