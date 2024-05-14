package org.palpalmans.ollive_back.domain.recipe.model.dto;

import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;

import java.util.List;

public record RecipeDto(
        Long id, String title, String thumbnail_url, String amount, String time, String difficulty, Double score, boolean isScraped, List<Recipe.Category> categories, List<Recipe.Ingredient> ingredients, List<Recipe.ProcessStep> processes
) {
}
