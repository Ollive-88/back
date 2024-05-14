package org.palpalmans.ollive_back.domain.recipe.repository;

import org.junit.jupiter.api.Test;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeSearchRequest;
import org.palpalmans.ollive_back.domain.recipe.model.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.palpalmans.ollive_back.domain.member.model.status.MemberRole.ROLE_REGISTERED_MEMBER;

@SpringBootTest
class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;


    private RecipeSearchRequest recipeSearchRequest() {
        List<String> havingIngredients = new ArrayList<>();
        List<String> dislikeIngredient = new ArrayList<>();
        havingIngredients.add("후추");
        havingIngredients.add("돼지고기");
        dislikeIngredient.add("오이");
        return new RecipeSearchRequest(
                "상황별", "일상", 0L, 10, havingIngredients, dislikeIngredient
        );
    }

    private Member member() {
        return Member.builder()
                .id(1)
                .email("doongdang@mevc.com")
                .gender("Male")
                .birthday(new Date())
                .name("조석현")
                .nickname("조석현")
                .role(ROLE_REGISTERED_MEMBER)
                .build();
    }

    @Test
    void dislikeIngredientCheckTest() {
        //given
        RecipeSearchRequest request = recipeSearchRequest();

        //when
        List<Recipe> recipes = recipeRepository.findRecipesByCriteriaWithPaging(request);

        //then
        for (Recipe recipe : recipes) {
            for (Recipe.Ingredient ingredient : recipe.getIngredients()) {
                assertNotEquals("오이", ingredient.getName());
            }
        }
    }


}