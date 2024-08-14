package com.kakaotech.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Place {
    @Id
    private String id; // 아직까지 생성될 일이 없어서 GeneratedValue 미적용
    @Column(name = "area_name")
    private String areaName;
    @Column(name = "road_name")
    private String roadName;
    @Column(name = "x_coord", updatable = false)
    private Double xCoord;
    @Column(name = "y_coord", updatable = false)
    private Double yCoord;
    @Column(name = "image_url")
    private String imageUrl;

}
