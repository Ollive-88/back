package org.palpalmans.ollive_back.domain.recipe.repository;

import org.palpalmans.ollive_back.domain.recipe.model.entity.RecipeScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeScoreRepository extends JpaRepository<RecipeScore, Long>{
    Optional<RecipeScore> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
}
