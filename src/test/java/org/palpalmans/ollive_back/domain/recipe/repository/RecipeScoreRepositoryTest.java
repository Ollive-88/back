package org.palpalmans.ollive_back.domain.recipe.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.palpalmans.ollive_back.domain.recipe.model.entity.RecipeScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.palpalmans.ollive_back.domain.member.model.status.MemberRole.ROLE_ADMIN;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RecipeScoreRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

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
    @Test
    void updateRecipeScoreTest() {
        //given
        RecipeScore recipeScore = RecipeScore.builder()
                .score(1)
                .recipeId(1L)
                .memberId(normalMember.getId())
                .build();

        RecipeScore oldRecipeScore = recipeScoreRepository.save(recipeScore);

        //when
        oldRecipeScore.updateScore(5);
        RecipeScore newRecipeScore = recipeScoreRepository.save(oldRecipeScore);

        //then
        assertThat(newRecipeScore.getScore()).isEqualTo(5);
        memberRepository.deleteAll();
    }
}
