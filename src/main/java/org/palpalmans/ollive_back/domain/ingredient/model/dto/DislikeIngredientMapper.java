package org.palpalmans.ollive_back.domain.ingredient.model.dto;

import org.palpalmans.ollive_back.domain.ingredient.model.entity.DislikeIngredient;

public class DislikeIngredientMapper {
    public static DislikeIngredientDto toDislikeIngredientDto(DislikeIngredient dislikeIngredient) {
        return new DislikeIngredientDto(dislikeIngredient.getId(), dislikeIngredient.getName());
    }
}
