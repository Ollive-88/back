package org.palpalmans.ollive_back.domain.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GeneratedToken { //record 타입으로 바꿀 수 있음
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public GeneratedToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
