package com.kakaotech.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SGG {
    @Id
    @Max(50)
    @NotNull
    private String id;

    @Column(name = "sido_code")
    @Max(10)
    private String sidoCode;

    @Column(name = "sgg_code")
    @Max(10)
    private String sggCode;

    @Column(name = "sido_name")
    @Max(100)
    @NotNull
    private String sidoName;

    @Column(name = "sgg_name")
    @Max(100)
    private String sggName;
}
