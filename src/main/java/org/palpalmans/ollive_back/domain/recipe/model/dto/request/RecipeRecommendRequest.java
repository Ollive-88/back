package org.palpalmans.ollive_back.domain.recipe.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeRecommendRequest(
        @NotEmpty
        List<String> havingIngredients,
        @NotNull
        List<String> dislikeIngredients
) {
}
