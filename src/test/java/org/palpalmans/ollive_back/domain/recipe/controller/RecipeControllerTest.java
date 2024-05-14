package org.palpalmans.ollive_back.domain.recipe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeScoreRequest;
import org.palpalmans.ollive_back.domain.recipe.model.dto.request.RecipeSearchRequest;
import org.palpalmans.ollive_back.domain.recipe.model.entity.RecipeScore;
import org.palpalmans.ollive_back.domain.recipe.repository.RecipeRepository;
import org.palpalmans.ollive_back.domain.recipe.repository.RecipeScoreRepository;
import org.palpalmans.ollive_back.domain.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.palpalmans.ollive_back.domain.member.model.status.MemberRole.ROLE_ADMIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeScoreRepository recipeScoreRepository;

    private Member member;
    private NormalMember normalMember;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("test@naver.com")
                .name("name")
                .nickname("nickname")
                .birthday(new Date())
                .profilePicture("profilePicture")
                .role(ROLE_ADMIN)
                .gender("male")
                .build();

        normalMember = new NormalMember(member, "1234");
        memberRepository.save(normalMember);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Transactional
    void getRecipeMetaDataTearDown() {
        recipeScoreRepository.deleteAll();

    }

    private RecipeScoreRequest recipeScoreRequest() {
        return new RecipeScoreRequest(1L, 1);
    }

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

    private RecipeSearchRequest recipeSearchRequestWithNoCategory() {
        List<String> havingIngredients = new ArrayList<>();
        List<String> dislikeIngredient = new ArrayList<>();
        havingIngredients.add("후추");
        havingIngredients.add("돼지고기");

        return new RecipeSearchRequest(
                "종류별", "", 0L, 10, havingIngredients, dislikeIngredient
        );
    }

    private String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

//    @Test
//    @WithUserDetails(value = "test@naver.com",
//            userDetailsServiceBeanName = "customMemberDetailsService",
//            setupBefore = TestExecutionEvent.TEST_EXECUTION
//    )
//    void getRecipeTest() throws Exception {
//        //given
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"));
//
//        //then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.title", notNullValue()))
//                .andExpect(jsonPath("$.thumbnail_url", notNullValue()))
//                .andExpect(jsonPath("$.isScraped", notNullValue()))
//                .andDo(print());
//    }

    @Test
    void getRecipesTest() throws Exception {
        //given
        RecipeSearchRequest request = recipeSearchRequest();

        //when
        ResultActions result = mockMvc.perform(
                post("/api/v1/recipes")
                        .contentType(APPLICATION_JSON)
                        .content(objectToJson(request))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void getRecipesWithNoCategoryTest() throws Exception {
        //given
        RecipeSearchRequest request = recipeSearchRequestWithNoCategory();

        //when
        ResultActions result = mockMvc.perform(
                post("/api/v1/recipes")
                        .contentType(APPLICATION_JSON)
                        .content(objectToJson(request))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "test@naver.com",
            userDetailsServiceBeanName = "customMemberDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void updateScoreTest() throws Exception {
        //given
        RecipeScore recipeScore = RecipeScore.builder()
                .score(5)
                .recipeId(1L)
                .memberId(normalMember.getId())
                .build();

        recipeScoreRepository.save(recipeScore);
        RecipeScoreRequest request = recipeScoreRequest();

        //when
        mockMvc.perform(post("/api/v1/recipes/scores")
                        .contentType(APPLICATION_JSON)
                        .content(objectToJson(request))
                ).andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());

        //then
        getRecipeMetaDataTearDown();
    }
}