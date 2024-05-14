package org.palpalmans.ollive_back.domain.cloth.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.palpalmans.ollive_back.common.BaseTimeEntity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "cloth")
@NoArgsConstructor(access = PROTECTED)
public class Cloth extends BaseTimeEntity {
    @Column(name = "super_category")
    String superCategory;
    @Column(name = "sub_category")
    String subCategory;
    @Column(name = "detail_category")
    String detailCategory;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "brand")
    private String brand;
    @Column(name = "brand_english")
    private String brandEnglish;
    @Column(name = "release_year")
    private Short releaseYear;
    @Column(name = "release_quarter", columnDefinition = "TINYINT")
    private Short releaseQuarter;
    @Column(name = "product_url")
    private String productUrl;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "spring", columnDefinition = "TINYINT(1)")
    private boolean spring;

    @Column(name = "summer", columnDefinition = "TINYINT(1)")
    private boolean summer;

    @Column(name = "fall", columnDefinition = "TINYINT(1)")
    private boolean fall;

    @Column(name = "winter", columnDefinition = "TINYINT(1)")
    private boolean winter;

    @Column(name = "casual", nullable = false)
    private double casual;

    @Column(name = "formal", nullable = false)
    private double formal;

    @Column(name = "fancy", nullable = false)
    private double fancy;

    @Column(name = "sporty", nullable = false)
    private double sporty;
}