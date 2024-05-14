package org.palpalmans.ollive_back.domain.ingredient.repository;

import org.palpalmans.ollive_back.domain.ingredient.model.entity.FridgeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {
    List<FridgeIngredient> findByMemberIdOrderByEndAtAsc(long memberId);
    Optional<FridgeIngredient> findByIdAndMemberId(long fridgeIngredientId, long memberId);
}
