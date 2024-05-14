package org.palpalmans.ollive_back.domain.ingredient.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DislikeIngredientRegisterRequest(
        @NotBlank
        String name
) {
}
