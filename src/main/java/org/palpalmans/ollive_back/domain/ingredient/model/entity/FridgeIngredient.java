package org.palpalmans.ollive_back.domain.ingredient.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FridgeIngredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate endAt;

    @Builder
    public FridgeIngredient(Long memberId, String name, LocalDate endAt) {
        this.memberId = memberId;
        this.name = name;
        this.endAt = endAt;
    }

    public void modifyName(String name){
        this.name = name;
    }

    public void modifyEndAt(LocalDate endAt){
        this.endAt = endAt;
    }
}
