package org.palpalmans.ollive_back.domain.cloth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("cloth.fastapi")
public class FastApiProperties {
    private final String baseUrl;
}
