package org.palpalmans.ollive_back.domain.ingredient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.FridgeIngredientDto;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.request.FridgeIngredientRequest;
import org.palpalmans.ollive_back.domain.ingredient.service.FridgeIngredientService;
import org.palpalmans.ollive_back.domain.ingredient.util.UtilMethods;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fridge-ingredients")
public class FridgeIngredientController {
    private final FridgeIngredientService fridgeIngredientService;

    @GetMapping
    public ResponseEntity<List<FridgeIngredientDto>> getFridgeIngredients(@AuthenticationPrincipal CustomMemberDetails customMemberDetails){
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(fridgeIngredientService.getFridgeIngredients(memberId));
    }

    @PostMapping
    public ResponseEntity<Long> registerFridgeIngredient(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @Valid @RequestBody FridgeIngredientRequest request
    ) {
        if(!UtilMethods.isValidLocalDate(request.endAt())){
           throw new RuntimeException("유효하지 않은 날짜 타입입니다");
        }

        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(fridgeIngredientService.registerFridgeIngredient(memberId, request));
    }

    @PutMapping("/{fridgeIngredientId}")
    public ResponseEntity<Long> modifyFridgeIngredient(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @Valid @RequestBody FridgeIngredientRequest request,
            @PathVariable Long fridgeIngredientId
    ){
        if(!UtilMethods.isValidLocalDate(request.endAt())){
            throw new RuntimeException("유효하지 않은 날짜 타입입니다");
        }

        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(fridgeIngredientService.modifyFridgeIngredient(memberId, fridgeIngredientId, request));
    }


    @DeleteMapping("/{fridgeIngredientId}")
    public ResponseEntity<Long> deleteFridgeIngredient(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @PathVariable Long fridgeIngredientId
    ){
        long memberId = customMemberDetails.getId();
        return ResponseEntity.ok().body(fridgeIngredientService.deleteFridgeIngredient(memberId, fridgeIngredientId));
    }
}
