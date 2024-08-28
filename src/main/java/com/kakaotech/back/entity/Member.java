package com.kakaotech.back.entity;

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
}
