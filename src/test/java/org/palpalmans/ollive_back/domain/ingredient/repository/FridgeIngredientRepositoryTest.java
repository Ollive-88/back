package org.palpalmans.ollive_back.domain.ingredient.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palpalmans.ollive_back.domain.ingredient.model.entity.FridgeIngredient;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.palpalmans.ollive_back.domain.member.model.status.MemberRole.ROLE_ADMIN;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class FridgeIngredientRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FridgeIngredientRepository fridgeIngredientRepository;

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

    @DisplayName("냉장고 재료 수정 테스트")
    @Test
    void modifyFridgeIngredientTest() {
        //given
        FridgeIngredient fridgeIngredient = fridgeIngredientRepository.save(new FridgeIngredient(normalMember.getId(), "감자", LocalDate.parse("2022-03-10")));

        //when
        fridgeIngredient.modifyEndAt(LocalDate.parse("2024-04-28"));
        fridgeIngredient.modifyName("간장");

        fridgeIngredientRepository.save(fridgeIngredient);

        //then
        assertThat(fridgeIngredient.getName()).isEqualTo("간장");
        assertThat(fridgeIngredient.getEndAt().toString()).isEqualTo("2024-04-28");

    }
}