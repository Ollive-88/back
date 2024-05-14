package org.palpalmans.ollive_back.domain.recipe.repository;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeRecommendRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeSearchRequest;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class CustomRecipeRepositoryImpl implements CustomRecipeRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Recipe> findRecipesByCriteriaWithPaging(RecipeSearchRequest recipeSearchRequest) {
        Long lastRecipeId = recipeSearchRequest.lastRecipeId();
        String recipeCase = recipeSearchRequest.recipeCase();
        String recipeCategory = recipeSearchRequest.recipeCategory();
        List<String> havingIngredients = recipeSearchRequest.havingIngredients();
        List<String> dislikeIngredients = recipeSearchRequest.dislikeIngredients();
        int size = recipeSearchRequest.size();

        Criteria criteria = Criteria.where("recipeId").gt(lastRecipeId);
        Criteria categoryCriteria = Criteria.where("recipe_case_name").is(recipeCase);

        if (!recipeCategory.isEmpty()) {
            categoryCriteria = categoryCriteria.and("name").is(recipeCategory);
        }

        criteria = criteria.and("categories").elemMatch(categoryCriteria);

        List<Criteria> andCriteriaList = new ArrayList<>(havingIngredients.stream()
                .map(ingredient -> Criteria.where("ingredients.name").regex(Pattern.quote(ingredient), "i"))
                .toList());

        if (!dislikeIngredients.isEmpty()) {
            Criteria notCriteria = Criteria.where("ingredients.name").nin(dislikeIngredients);
            andCriteriaList.add(notCriteria);
        }

        if (!andCriteriaList.isEmpty()) {
            criteria = criteria.andOperator(andCriteriaList.toArray(new Criteria[0]));
        }


        Query query = Query.query(criteria).limit(size);
        return mongoTemplate.find(query, Recipe.class);
    }

    @Override
    public List<Recipe> findRecipesByIngredientsAndScoredRecipeIds(RecipeRecommendRequest request, List<Long> scoredRecipeIds){
        List<String> havingIngredients = request.havingIngredients();
        List<String> dislikeIngredients = request.dislikeIngredients();

        Criteria criteria = new Criteria();

        List<Criteria> andCriteriaList = new ArrayList<>(havingIngredients.stream()
                .map(ingredient -> Criteria.where("ingredients.name").regex(Pattern.quote(ingredient), "i"))
                .toList());


        if (!dislikeIngredients.isEmpty()) {
            Criteria notCriteria = Criteria.where("ingredients.name").nin(dislikeIngredients);
            andCriteriaList.add(notCriteria);
        }

        if (!scoredRecipeIds.isEmpty()) {
            Criteria excludeCriteria = new Criteria("recipeId").nin(scoredRecipeIds);
            andCriteriaList.add(excludeCriteria);
        }

        if (!andCriteriaList.isEmpty()) {
            criteria = criteria.andOperator(andCriteriaList.toArray(new Criteria[0]));
        }

        Query query = Query.query(criteria).limit(100);
        return mongoTemplate.find(query, Recipe.class);
    }

    @Override
    public List<Recipe> findRecipesByScoredIds(List<Long> scoredRecipeIds){
        Random random = new Random();
        Criteria criteria = Criteria.where("recipeId").gt(random.nextInt(190000)+1);
        if (!scoredRecipeIds.isEmpty()){
            Criteria exCriteria = new Criteria("recipeId").nin(scoredRecipeIds);
            criteria.andOperator(exCriteria);
        }
        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "score")).limit(50);
        return mongoTemplate.find(query, Recipe.class);
    }

    @Override
    public List<Recipe> findRecipesByScrapedIds(List<Long> scrapedRecipeIds, Long lastRecipeId, int size) {
        Criteria criteria = new Criteria();

        if(lastRecipeId > 0){
            Criteria idCriteria = Criteria.where("recipeId").gt(lastRecipeId);
            criteria.andOperator(idCriteria);
        }

        if (!scrapedRecipeIds.isEmpty()){
            Criteria exCriteria = new Criteria("recipeId").in(scrapedRecipeIds);
            criteria.andOperator(exCriteria);
        }

        Query query = Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "recipeId")).limit(size);

        return mongoTemplate.find(query, Recipe.class);
    }
}
