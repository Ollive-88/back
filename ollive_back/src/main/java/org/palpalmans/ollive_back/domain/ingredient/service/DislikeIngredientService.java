package org.palpalmans.ollive_back.domain.ingredient.service;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.DislikeIngredientDto;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.DislikeIngredientMapper;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.request.DislikeIngredientRegisterRequest;
import org.palpalmans.ollive_back.domain.ingredient.model.entity.DislikeIngredient;
import org.palpalmans.ollive_back.domain.ingredient.repository.DislikeIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DislikeIngredientService {
    private final DislikeIngredientRepository dislikeIngredientRepository;

    public List<DislikeIngredientDto> getDislikeIngredients(long memberId) {
        List<DislikeIngredient> dislikeIngredients = dislikeIngredientRepository.findByMemberId(memberId);

        return dislikeIngredients.stream().map(DislikeIngredientMapper::toDislikeIngredientDto).toList();
    }

    @Transactional
    public Long registerDislikeIngredient(long memberId, DislikeIngredientRegisterRequest request) {
        DislikeIngredient dislikeIngredient = new DislikeIngredient(memberId, request.name());
        return dislikeIngredientRepository.save(dislikeIngredient).getId();
    }
}
