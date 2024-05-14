package org.palpalmans.ollive_back.domain.recipe.repository;

import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecipeRepository extends MongoRepository<Recipe, Long>, CustomRecipeRepository{
    Optional<Recipe> findByRecipeId(Long recipeId);
}
