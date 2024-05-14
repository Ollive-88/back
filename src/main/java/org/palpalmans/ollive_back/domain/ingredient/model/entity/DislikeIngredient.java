package org.palpalmans.ollive_back.domain.ingredient.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DislikeIngredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column(length = 50)
    private String name;

    @Builder
    public DislikeIngredient(long memberId, String name){
        this.memberId = memberId;
        this.name = name;
    }
}
