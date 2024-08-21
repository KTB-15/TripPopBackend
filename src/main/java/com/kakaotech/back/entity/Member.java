package com.kakaotech.back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Member {
    @Id
    private String id;

    @Column(name = "member_id", unique = true)
    private String memberId;

    private String password;

    private String nickname;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotBlank
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "travel_like_sgg")
    private SGG travelLikeSGG;

    @Column(name = "travel_style_1")
    private String travelStyle1;

    @Column(name = "travel_style_2")
    private String travelStyle2;

    @Column(name = "travel_style_3")
    private String travelStyle3;

    @Column(name = "travel_style_4")
    private String travelStyle4;

    @Column(name = "travel_style_5")
    private String travelStyle5;

    @Column(name = "travel_style_6")
    private String travelStyle6;

    @Column(name = "travel_style_7")
    private String travelStyle7;

    @Column(name = "travel_style_8")
    private String travelStyle8;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime registerAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Visit> visits;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Favourite> favourites;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<History> histories;
}
