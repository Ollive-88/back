package org.palpalmans.ollive_back.domain.recipe.model.dto;

import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;

public class RecipeMapper {
    public static RecipeDto toRecipeDto(Recipe recipe, boolean isScraped){
        return new RecipeDto(
                recipe.getRecipeId(), recipe.getTitle(), recipe.getThumbnailUrl(), recipe.getAmount(),
                recipe.getTime(), recipe.getDifficulty(), recipe.getScore(), isScraped, recipe.getCategories(), recipe.getIngredients(), recipe.getProcess()
        );
    }

    public static RecipeSummaryDto toRecipeSummaryDto(Recipe recipe){
        return new RecipeSummaryDto(
                recipe.getRecipeId(), recipe.getTitle(), recipe.getThumbnailUrl()
        );
    }
}
