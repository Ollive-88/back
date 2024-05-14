package org.palpalmans.ollive_back.domain.cloth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherResponse(Response response) {
    public record Response(@JsonProperty("header") WeatherResponseHeader weatherResponseHeader,
                           @JsonProperty("body") WeatherResponseBody weatherResponseBody) {
    }

    public record WeatherResponseHeader(@JsonProperty("resultCode") String resultCode,
                                        @JsonProperty("resultMsg") String resultMsg) {
    }

    public record WeatherResponseBody(@JsonProperty("dataType") String dataType,
                                      @JsonProperty("items") Items items) {
    }

    public record Items(@JsonProperty("item") List<Item> itemList) {
    }

    public record Item(String baseDate, String baseTime, String category,
                       String nx, String ny, String obsrValue) {
    }
}