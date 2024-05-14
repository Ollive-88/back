package org.palpalmans.ollive_back.domain.recipe.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeSearchRequest(
        @NotNull
        String recipeCase,
        @NotNull
        String recipeCategory,
        @NotNull
        Long lastRecipeId,
        int size,
        @NotEmpty
        List<String> havingIngredients,
        @NotNull
        List<String> dislikeIngredients
) {
}
