package com.kakaotech.back.entity;

import com.kakaotech.back.dto.member.MemberSurveyDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Member {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "member_id", unique = true)
    @NotBlank(message = "Member ID cannot be blank")
    private String memberId;

    @NotBlank
    private String password;

    private String nickname;

    @NotNull(message = "Gender cannot be null")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Age cannot be null")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "travel_like_sgg")
    private SGG travelLikeSGG;

    @Column(name = "travel_style_1")
    private Integer travelStyle1;

    @Column(name = "travel_style_2")
    private Integer travelStyle2;

    @Column(name = "travel_style_3")
    private Integer travelStyle3;

    @Column(name = "travel_style_4")
    private Integer travelStyle4;

    @Column(name = "travel_style_5")
    private Integer travelStyle5;

    @Column(name = "travel_style_6")
    private Integer travelStyle6;

    @Column(name = "travel_style_7")
    private Integer travelStyle7;

    @Column(name = "travel_style_8")
    private Integer travelStyle8;

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

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public void updateStyle(MemberSurveyDto dto){
        this.travelStyle1 = dto.travelStyle1();
        this.travelStyle2 = dto.travelStyle2();
        this.travelStyle3 = dto.travelStyle3();
        this.travelStyle4 = dto.travelStyle4();
        this.travelStyle5 = dto.travelStyle5();
        this.travelStyle6 = dto.travelStyle6();
        this.travelStyle7 = dto.travelStyle7();
        this.travelStyle8 = dto.travelStyle8();
    }
}
