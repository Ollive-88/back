package org.palpalmans.ollive_back.domain.recipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeDto;
import org.palpalmans.ollive_back.domain.recipe.model.dto.RecipeSummaryDto;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeRecommendRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeScoreRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeSearchRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.ScrapRequest;
import org.palpalmans.ollive_back.domain.recipe.service.RecipeRecommendService;
import org.palpalmans.ollive_back.domain.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
@Validated
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeRecommendService recipeRecommendService;

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipe(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @PathVariable Long recipeId
    ) {
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(recipeService.getRecipe(memberId, recipeId));
    }

    @GetMapping("/scraps")
    public ResponseEntity<List<RecipeSummaryDto>> getScrapedRecipes(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @RequestParam Long lastRecipeId,
            @RequestParam int size
    ) {
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(recipeService.getScrapedRecipes(memberId, lastRecipeId, size));
    }

    @PostMapping
    public ResponseEntity<List<RecipeSummaryDto>> getRecipesByCategory(@Valid @RequestBody RecipeSearchRequest recipeSearchRequest) {
        return ResponseEntity.ok().body(recipeService.getRecipesByCategory(recipeSearchRequest));
    }

    @PostMapping("/scores")
    public ResponseEntity<Integer> generateOrUpdateScore(
            @Valid @RequestBody RecipeScoreRequest recipeScoreRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Long memberId = customMemberDetails.getId();
        return ResponseEntity.ok().body(recipeService.generateOrUpdateScore(memberId, recipeScoreRequest));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<List<RecipeSummaryDto>> recommendRecipes(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @Valid @RequestBody RecipeRecommendRequest request
    ){
        long memberId = customMemberDetails.getId();
        return ResponseEntity.ok().body(recipeRecommendService.getRecommendCandidates(memberId, request));
    }

    @PostMapping("/scraps")
    public ResponseEntity<Long> scrapOrUnScrapRecipe(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @Valid @RequestBody ScrapRequest request
    ){
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(recipeService.scrapOrUnScrapRecipe(memberId, request));
    }
}
