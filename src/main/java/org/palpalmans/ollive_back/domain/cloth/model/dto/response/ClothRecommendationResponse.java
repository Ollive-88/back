package org.palpalmans.ollive_back.domain.cloth.model.dto.response;

import java.util.List;

public record ClothRecommendationResponse(
        List<ClothResponse> outer,
        List<ClothResponse> top,
        List<ClothResponse> bottom,
        List<ClothResponse> shoes
) {

}