package org.palpalmans.ollive_back.domain.recipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeMapper;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeSummaryDto;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeRecommendRequest;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import org.palpalmans.ollive_back.domain.recipe.model.entity.RecipeScore;
import org.palpalmans.ollive_back.domain.recipe.repository.RecipeRepository;
import org.palpalmans.ollive_back.domain.recipe.repository.RecipeScoreRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeRecommendService {
    private final RecipeScoreRepository recipeScoreRepository;
    private final RecipeRepository recipeRepository;

    public List<RecipeSummaryDto> getRecommendCandidates(
            Long memberId,
            RecipeRecommendRequest request){

        List<Long> scoredRecipeIds = recipeScoreRepository.findByMemberId(memberId).stream().map(RecipeScore::getRecipeId).toList();
        List<Recipe> recommendCandidates = recipeRepository.findRecipesByIngredientsAndScoredRecipeIds(request, scoredRecipeIds);

        if (recommendCandidates.size() <= 10){
            List<Recipe> sortByScore = recipeRepository.findRecipesByScoredIds(scoredRecipeIds);

            return sortByScore.stream()
                    .map(RecipeMapper::toRecipeSummaryDto)
                    .toList();
        }else{
            Collections.shuffle(recommendCandidates);
            return recommendCandidates.stream()
                    .map(RecipeMapper::toRecipeSummaryDto)
                    .toList().subList(0, 10);
        }
    }
}
