package org.palpalmans.ollive_back.domain.recipe.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RecipeScoreRequest(
        @NotNull
        Long recipeId,
        @Min(1)@Max(5)
        int score
) {
}
