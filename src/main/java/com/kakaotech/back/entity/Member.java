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
import java.util.Set;
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

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    @Builder.Default()
    private Gender gender = Gender.MALE;

    @Builder.Default()
    private Integer age = 20;

    @Column(name = "travel_style_1")
    @Builder.Default()
    private Integer travelStyle1 = 4;

    @Column(name = "travel_style_2")
    @Builder.Default()
    private Integer travelStyle2 = 4;

    @Column(name = "travel_style_3")
    @Builder.Default()
    private Integer travelStyle3 = 4;

    @Column(name = "travel_style_4")
    @Builder.Default()
    private Integer travelStyle4 = 4;

    @Column(name = "travel_style_5")
    @Builder.Default()
    private Integer travelStyle5 = 4;

    @Column(name = "travel_style_6")
    @Builder.Default()
    private Integer travelStyle6 = 4;

    @Column(name = "travel_style_7")
    @Builder.Default()
    private Integer travelStyle7 = 4;

    @Column(name = "travel_style_8")
    @Builder.Default()
    private Integer travelStyle8 = 4;

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

    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public void updateStyle(MemberSurveyDto dto){
        this.travelStyle1 = dto.travelStyle1();
        this.travelStyle2 = dto.travelStyle2();
        this.travelStyle4 = dto.travelStyle4();
        this.travelStyle5 = dto.travelStyle5();
        this.travelStyle6 = dto.travelStyle6();
        this.travelStyle7 = dto.travelStyle7();
        this.travelStyle8 = dto.travelStyle8();
    }
}
