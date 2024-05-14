package org.palpalmans.ollive_back.domain.recipe.model.dto.request;

import jakarta.validation.constraints.NotNull;

public record ScrapRequest(
        @NotNull
        Long recipeId
) {
}
