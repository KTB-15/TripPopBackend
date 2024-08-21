package com.kakaotech.back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @Column(name = "avg_rating")
    private Double avgRating;
}
