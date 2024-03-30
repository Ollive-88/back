package org.palpalmans.ollive_back.domain.fridge.service;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.fridge.model.dto.FridgeIngredientDto;
import org.palpalmans.ollive_back.domain.fridge.model.dto.FridgeIngredientMapper;
import org.palpalmans.ollive_back.domain.fridge.model.dto.request.FridgeIngredientRequest;
import org.palpalmans.ollive_back.domain.fridge.model.entity.FridgeIngredient;
import org.palpalmans.ollive_back.domain.fridge.repository.FridgeIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeIngredientService {
    private final FridgeIngredientRepository fridgeIngredientRepository;

    public List<FridgeIngredientDto> getFridgeIngredients(long memberId) {
        List<FridgeIngredient> fridgeIngredients = fridgeIngredientRepository.findByMemberIdOrderByEndAtAsc(memberId);

        return fridgeIngredients.stream().map(FridgeIngredientMapper::toFridgeIngredientDto).toList();
    }

    @Transactional
    public Long registerIngredient(long memberId, FridgeIngredientRequest request){
        FridgeIngredient fridgeIngredient = FridgeIngredient.builder()
                .memberId(memberId)
                .name(request.name())
                .endAt(LocalDate.parse(request.endAt()))
                .build();

        return fridgeIngredientRepository.save(fridgeIngredient).getId();
    }
}
