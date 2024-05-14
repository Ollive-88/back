package org.palpalmans.ollive_back.domain.recipe.repository;

import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeRecommendRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeSearchRequest;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import java.util.List;

public interface CustomRecipeRepository {
    List<Recipe> findRecipesByCriteriaWithPaging(RecipeSearchRequest recipeSearchRequest);
    List<Recipe> findRecipesByIngredientsAndScoredRecipeIds(RecipeRecommendRequest request, List<Long> scoredRecipeIds);

    List<Recipe> findRecipesByScoredIds(List<Long> scoredRecipeIds);
    List<Recipe> findRecipesByScrapedIds(List<Long> scrapedRecipeIds, Long lastRecipeId, int size);
}