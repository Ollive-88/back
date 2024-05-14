package org.palpalmans.ollive_back.domain.cloth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ClothResponse(
        @JsonProperty("id")
        Long id,
        @JsonProperty("rank")
        Integer rank,
        @JsonProperty("product_name")
        String productName,
        @JsonProperty("brand")
        String brand,
        @JsonProperty("brand_english")
        String brandEnglish,
        @JsonProperty("product_url")
        String productUrl,
        @JsonProperty("img_url")
        String imgUrl
) {
}
