package org.palpalmans.ollive_back.domain.image.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.palpalmans.ollive_back.common.BaseTimeEntity;
import org.palpalmans.ollive_back.domain.image.model.ImageType;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column(length = 20)
    @Enumerated(value = STRING)
    private ImageType imageType;

    @Column
    private Long referenceId;

    public Image(Long id, String address, ImageType imageType, Long referenceId) {
        this.id = id;
        this.address = address;
        this.imageType = imageType;
        this.referenceId = referenceId;
    }

    public Image(String address, ImageType imageType, Long referenceId) {
        this.address = address;
        this.imageType = imageType;
        this.referenceId = referenceId;
    }
}
