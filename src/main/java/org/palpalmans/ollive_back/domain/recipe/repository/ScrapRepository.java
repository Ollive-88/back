package org.palpalmans.ollive_back.domain.recipe.repository;

import org.palpalmans.ollive_back.domain.recipe.model.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByMemberIdAndRecipeId(long memberId, Long recipeId);

    List<Scrap> findByMemberId(long memberId);
}
