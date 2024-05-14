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
public class RecipeScore {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private Long recipeId;

    @Column(nullable = false)
    private Long memberId;

    @Builder
    public RecipeScore(int score, Long recipeId, Long memberId){
      this.score = score;
      this.recipeId = recipeId;
      this.memberId = memberId;
    }

    public void updateScore(int score){
        this.score = score;
    }
}
