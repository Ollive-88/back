package org.palpalmans.ollive_back.domain.cloth.model.dto.request;

import java.util.Objects;

public record ClothRecommendationRequest(String text, int longitude, int latitude) {
    public ClothRecommendationRequest {
        Objects.requireNonNull(text);
    }
    public WeatherRequest extractWeatherRequest(){
        return new WeatherRequest(longitude, latitude);
    }
}