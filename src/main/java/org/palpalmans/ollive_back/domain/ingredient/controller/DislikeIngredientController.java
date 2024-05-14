package org.palpalmans.ollive_back.domain.ingredient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.DislikeIngredientDto;
import org.palpalmans.ollive_back.domain.ingredient.model.dto.request.DislikeIngredientRegisterRequest;
import org.palpalmans.ollive_back.domain.ingredient.service.DislikeIngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dislike-ingredients")
public class DislikeIngredientController {
    private final DislikeIngredientService dislikeIngredientService;

    @GetMapping
    public ResponseEntity<List<DislikeIngredientDto>> getDislikeIngredients(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ){
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(dislikeIngredientService.getDislikeIngredients(memberId));
    }

    @PostMapping
    public ResponseEntity<Long> registerDislikeIngredient(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @Valid @RequestBody DislikeIngredientRegisterRequest request
    ){
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(dislikeIngredientService.registerDislikeIngredient(memberId, request));
    }

    @DeleteMapping("/{dislikeIngredientId}")
    public ResponseEntity<Long> deleteDislikeIngredient(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @PathVariable Long dislikeIngredientId
    ){
        long memberId = customMemberDetails.getId();

        return ResponseEntity.ok().body(dislikeIngredientService.deleteDislikeIngredient(memberId, dislikeIngredientId));
    }
}
