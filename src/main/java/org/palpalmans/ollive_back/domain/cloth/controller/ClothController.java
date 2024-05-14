package org.palpalmans.ollive_back.domain.cloth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.cloth.model.dto.request.ClothRecommendationRequest;
import org.palpalmans.ollive_back.domain.cloth.model.dto.response.ClothRecommendationResponse;
import org.palpalmans.ollive_back.domain.cloth.model.dto.response.ClothResponse;
import org.palpalmans.ollive_back.domain.cloth.model.dto.response.GetClothesResponse;
import org.palpalmans.ollive_back.domain.cloth.service.ClothService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cloth")
public class ClothController {
    private final ClothService clothService;

    @PostMapping("/recommendation")
    public ResponseEntity<ClothRecommendationResponse> recommendCloth(
            @RequestBody ClothRecommendationRequest clothRecommendationRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        return ResponseEntity.ok(clothService.recommendCloth(clothRecommendationRequest, customMemberDetails.getMember()));
    }

    @PostMapping("/{clothId}")
    public ResponseEntity<ClothResponse> seenCloth(@PathVariable(name="clothId") Long clothId,
                                                   @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        clothService.seenCloth(clothId, customMemberDetails.getMember());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/seen")
    public ResponseEntity<GetClothesResponse> getSeenCloth(@RequestParam int lastIndex, @RequestParam int size,
                                                           @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return ResponseEntity.ok( clothService.getSeenCloth(lastIndex, size,customMemberDetails.getMember()));
    }
}
