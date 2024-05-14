package org.palpalmans.ollive_back.domain.ingredient.model.dto;

public record FridgeIngredientDto(
    Long fridgeIngredientId,
    String name,
    String endAt
) {
}
