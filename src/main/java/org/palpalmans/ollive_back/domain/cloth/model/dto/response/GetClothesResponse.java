package org.palpalmans.ollive_back.domain.cloth.model.dto.response;

import java.util.List;

public record GetClothesResponse(
        List<ClothResponse> clothes
) {
}
