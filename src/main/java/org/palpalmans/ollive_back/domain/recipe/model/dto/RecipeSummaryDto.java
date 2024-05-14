package org.palpalmans.ollive_back.domain.recipe.model.dto;

public record RecipeSummaryDto (
        Long recipeId, String title, String thumbnail_url
) {
}
