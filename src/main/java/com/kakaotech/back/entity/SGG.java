package com.kakaotech.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "sgg")
public class SGG {

    @Id
    @Size(max = 50)
    @NotNull
    private String id;

    @Column(name = "sido_code")
    @Size(max = 10)
    private String sidoCode;

    @Column(name = "sgg_code")
    @Size(max = 10)
    private String sggCode;

    @Column(name = "sido_name")
    @Size(max = 100)
    @NotNull
    private String sidoName;

    @Column(name = "sgg_name")
    @Size(max = 100)
    private String sggName;
}
