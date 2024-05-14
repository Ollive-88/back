package org.palpalmans.ollive_back.domain.cloth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("cloth.weather")
public class WeatherApiProperties {
    private final String baseUrl;
    private final String serviceKey;
}
