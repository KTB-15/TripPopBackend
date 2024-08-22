package com.kakaotech.back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SGG {

    @Id
    @Size(max = 50)
    @NotNull
    // SGG_CD1 + SGG_CD2(SIDO_CODE + SGG_CODE)
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

    @OneToMany(mappedBy = "travelLikeSGG")
    private List<Member> members;
}
