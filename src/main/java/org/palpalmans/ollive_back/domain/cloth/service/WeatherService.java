package org.palpalmans.ollive_back.domain.cloth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.cloth.config.WeatherApiProperties;
import org.palpalmans.ollive_back.domain.cloth.model.dto.request.WeatherRequest;
import org.palpalmans.ollive_back.domain.cloth.model.dto.response.WeatherResponse;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherApiProperties weatherApiProperties;

    public double getTemperatureFromKMA(WeatherRequest weatherRequest) {
        LocalDateTime baseDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(60L);
        String baseDate = baseDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = baseDateTime.format(DateTimeFormatter.ofPattern("HHmm"));

        RestClient restClient = RestClient
                .builder()
                .requestFactory(ClientHttpRequestFactories
                        .get(ClientHttpRequestFactorySettings.DEFAULTS
                                .withConnectTimeout(Duration.ofSeconds(3))
                                .withReadTimeout(Duration.ofSeconds(3))))
                .build();




        try {
            WeatherResponse responseBody = restClient
                    .get()
                    .uri(buildTemperatureRequestUri(weatherRequest, baseDate, baseTime))
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(WeatherResponse.class);

            return Objects
                    .requireNonNull(responseBody)
                    .response()
                    .weatherResponseBody()
                    .items()
                    .itemList()
                    .stream()
                    .filter(item -> item.category().equals("T1H"))
                    .map(item -> Double.parseDouble(item.obsrValue()))
                    .findFirst().orElseThrow();
        } catch (Exception e) {
            int month = Integer.parseInt(baseDate.substring(4, 6));
            return switch (month) {
                case 1, 2 -> -10;
                case 3, 4 -> 18;
                case 5, 6 -> 25;
                case 7, 8 -> 30;
                case 9, 10 -> 15;
                case 11, 12 -> 5;
                default -> 20;
            };
        }
    }

    public URI buildTemperatureRequestUri(WeatherRequest weatherRequest, String baseDate, String baseTime) {
        WeatherRequest grid = convertFromCoordinateToGrid(weatherRequest);

        log.info("baseDate: {}", baseDate);
        log.info("baseTime: {}", baseTime);

        return UriComponentsBuilder.fromUriString(weatherApiProperties.getBaseUrl())
                .queryParam("serviceKey", URLEncoder.encode(weatherApiProperties.getServiceKey(), StandardCharsets.UTF_8))
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", (int) grid.longitude())
                .queryParam("ny", (int) grid.latitude())
                .queryParam("dataType", "JSON")
                .build(true)
                .toUri();
    }

    public WeatherRequest convertFromCoordinateToGrid(WeatherRequest weatherRequest) {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기준점 Y좌표(GRID)
        double DEGRAD = Math.PI / 180.0;
        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double lat_X = weatherRequest.latitude();
        double lng_Y = weatherRequest.longitude();

        double tan = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / tan;
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = tan;
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);

        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lng_Y * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        double xGrid = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        double yGrid = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return new WeatherRequest(xGrid, yGrid);
    }
}
