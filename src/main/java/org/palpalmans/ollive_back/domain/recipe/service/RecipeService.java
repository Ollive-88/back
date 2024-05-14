package org.palpalmans.ollive_back.domain.recipe.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeDto;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeMapper;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeSummaryDto;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeScoreRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeSearchRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.ScrapRequest;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import org.palpalmans.ollive_back.domain.recipe.model.entity.RecipeScore;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Scrap;
import org.palpalmans.ollive_back.domain.recipe.repository.RecipeRepository;
import org.palpalmans.ollive_back.domain.recipe.repository.RecipeScoreRepository;
import org.palpalmans.ollive_back.domain.recipe.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
	private final RecipeScoreRepository recipeScoreRepository;
	private final ScrapRepository scrapRepository;

	public RecipeDto getRecipe(Long memberId, Long recipeId) {
		Recipe recipe = recipeRepository.findByRecipeId(recipeId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 레시피 입니다."));
		boolean isScraped = scrapRepository.findByMemberIdAndRecipeId(memberId, recipeId).isPresent();

		return RecipeMapper.toRecipeDto(recipe, isScraped);
	}

	 public List<RecipeSummaryDto> getRecipesByCategory(RecipeSearchRequest recipeSearchRequest) {
		 List<Recipe> recipes = recipeRepository.findRecipesByCriteriaWithPaging(recipeSearchRequest);

		 return recipes.stream().map(RecipeMapper::toRecipeSummaryDto).toList();
	 }

	 @Transactional
	 public int generateOrUpdateScore(Long memberId, RecipeScoreRequest recipeScoreRequest){
		Long recipeId = recipeScoreRequest.recipeId();
		int score = recipeScoreRequest.score();

		 Optional<RecipeScore> optionalRecipeScore = recipeScoreRepository.findByMemberIdAndRecipeId(memberId, recipeId);

		 if (optionalRecipeScore.isPresent()){
			 RecipeScore recipeScore = optionalRecipeScore.get();
			 recipeScore.updateScore(score);
			 return recipeScoreRepository.save(recipeScore).getScore();
		 }else{
			 RecipeScore newRecipeScore = RecipeScore.builder()
					 .score(score)
					 .recipeId(recipeId)
					 .memberId(memberId)
					 .build();

			 return recipeScoreRepository.save(newRecipeScore).getScore();
		 }
	 }

	 @Transactional
    public Long scrapOrUnScrapRecipe(long memberId, ScrapRequest request) {
		Long recipeId = request.recipeId();
		Optional<Scrap> scrapedRecipe = scrapRepository.findByMemberIdAndRecipeId(memberId, recipeId);

		if(scrapedRecipe.isPresent()){
			Long scrapId = scrapedRecipe.get().getId();
			scrapRepository.deleteById(scrapId);

			return scrapId;
		}else{
			return scrapRepository.save(new Scrap(memberId, recipeId)).getId();
		}
	}

	public List<RecipeSummaryDto> getScrapedRecipes(long memberId, Long lastRecipeId, int size) {
		List<Scrap> scrapedRecipes = scrapRepository.findByMemberId(memberId);

		if(scrapedRecipes.isEmpty()){
			return new ArrayList<>();
		}

		List<Long> scrapedRecipeIds = scrapedRecipes.stream().map(Scrap::getRecipeId).toList();
		List<Recipe> recipesByScoredIds = recipeRepository.findRecipesByScrapedIds(scrapedRecipeIds, lastRecipeId, size);

		return recipesByScoredIds.stream().map(RecipeMapper::toRecipeSummaryDto).toList();
	}
}
