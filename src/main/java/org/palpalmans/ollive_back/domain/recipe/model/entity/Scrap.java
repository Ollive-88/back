package org.palpalmans.ollive_back.domain.recipe.model.entity;

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
public class Scrap {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column
    private Long recipeId;

    @Builder
    public Scrap(Long memberId, Long recipeId){
        this.memberId = memberId;
        this.recipeId = recipeId;
    }
}
