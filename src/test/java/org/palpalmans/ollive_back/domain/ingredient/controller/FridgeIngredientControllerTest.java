package org.palpalmans.ollive_back.domain.ingredient.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.request.FridgeIngredientRequest;
import org.palpalmans.ollive_back.domain.ingredient.model.entity.FridgeIngredient;
import org.palpalmans.ollive_back.domain.ingredient.repository.FridgeIngredientRepository;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.palpalmans.ollive_back.domain.member.model.status.MemberRole.ROLE_ADMIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FridgeIngredientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FridgeIngredientRepository fridgeIngredientRepository;

    Member member;
    NormalMember normalMember;

    private String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private FridgeIngredientRequest request(){
        return new FridgeIngredientRequest("감자", "2022-03-10");
    }

    void fridgeIngredientSetup(List<FridgeIngredient> list) {
        FridgeIngredient fridgeIngredient1 = fridgeIngredientRepository.save(new FridgeIngredient(normalMember.getId(), "감자", LocalDate.of(2024, 5, 1)));
        FridgeIngredient fridgeIngredient2 = fridgeIngredientRepository.save(new FridgeIngredient(normalMember.getId(), "가지", LocalDate.of(2024, 4, 2)));
        FridgeIngredient fridgeIngredient3 = fridgeIngredientRepository.save(new FridgeIngredient(normalMember.getId(), "소고기", LocalDate.of(2024, 7, 3)));
        FridgeIngredient fridgeIngredient4 = fridgeIngredientRepository.save(new FridgeIngredient(normalMember.getId(), "양파", LocalDate.of(2024, 8, 4)));

        list.add(fridgeIngredient1);
        list.add(fridgeIngredient2);
        list.add(fridgeIngredient3);
        list.add(fridgeIngredient4);
    }

    void fridgeIngredientTearDown() {
        fridgeIngredientRepository.deleteAll();
    }


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

    @DisplayName("냉장고 재료 목록 조회 테스트")
    @Test
    @WithUserDetails(value = "test@naver.com",
            userDetailsServiceBeanName = "customMemberDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getFridgeIngredientsTest() throws Exception{
        //given
        List<FridgeIngredient> list = new ArrayList<>();
        fridgeIngredientSetup(list);
        list.sort(new Comparator<FridgeIngredient>() {
            @Override
            public int compare(FridgeIngredient o1, FridgeIngredient o2) {
                return o1.getEndAt().compareTo(o2.getEndAt()) ;
            }
        });

        //when
        mockMvc.perform(get("/api/v1/fridge-ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fridgeIngredientId").value(list.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(list.get(0).getName()))
                .andExpect(jsonPath("$[1].fridgeIngredientId").value(list.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(list.get(1).getName()))
                .andExpect(jsonPath("$[2].fridgeIngredientId").value(list.get(2).getId()))
                .andExpect(jsonPath("$[2].name").value(list.get(2).getName()))
                .andExpect(jsonPath("$[3].fridgeIngredientId").value(list.get(3).getId()))
                .andExpect(jsonPath("$[3].name").value(list.get(3).getName()))
                .andDo(print());

        //then
        fridgeIngredientTearDown();
    }



    @DisplayName("냉장고 재료 저장 테스트")
    @Test
    @WithUserDetails(value = "test@naver.com",
            userDetailsServiceBeanName = "customMemberDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void registerFridgeIngredientTest() throws Exception {
        //given
        FridgeIngredientRequest request = request();

        //when
        mockMvc.perform(post("/api/v1/fridge-ingredients").contentType(APPLICATION_JSON).content(objectToJson(request)))
                .andExpect(status().isOk())
                .andDo(print());
        //then
    }

    @DisplayName("냉장고 재료 수정 테스트")
    @Test
    @WithUserDetails(value = "test@naver.com",
            userDetailsServiceBeanName = "customMemberDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void modifyFridgeIngredientTest() throws Exception {
        //given
        List<FridgeIngredient> list = new ArrayList<>();
        fridgeIngredientSetup(list);

        //when
        mockMvc.perform(
                        put("/api/v1/fridge-ingredients/{fridgeIngredientId}",
                        list.get(0).getId()).contentType(APPLICATION_JSON).content(objectToJson(request()))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(list.get(0).getId().toString()))
                .andDo(print());

        //then
        fridgeIngredientTearDown();
    }

    @DisplayName("냉장고 재료 삭제 테스트")
    @Test
    @WithUserDetails(value = "test@naver.com",
            userDetailsServiceBeanName = "customMemberDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void deleteFridgeIngredientTest() throws Exception {
        //given
        List<FridgeIngredient> list = new ArrayList<>();
        fridgeIngredientSetup(list);

        //when
        mockMvc.perform(delete("/api/v1/fridge-ingredients/{fridgeIngredientId}", list.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(list.get(0).getId().toString()))
                .andDo(print());

        //then
        fridgeIngredientTearDown();
    }
}