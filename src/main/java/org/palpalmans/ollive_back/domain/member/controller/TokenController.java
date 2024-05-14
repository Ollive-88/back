package org.palpalmans.ollive_back.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.common.security.service.JwtService;
import org.palpalmans.ollive_back.domain.member.model.dto.request.TokenCreateRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final JwtService jwtService;

    @PostMapping("/api/auth/token/refresh")
    public ResponseEntity<String> issueRefreshToken(@RequestHeader("Refresh") String refreshToken ){

        String token = refreshToken.split(" ")[1];

        if(jwtService.isExpired(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }else {
            //토큰에서 정보뽑기
            TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();
            tokenCreateRequest.setId(jwtService.getId(token));
            tokenCreateRequest.setRole("ROLE_REGISTERTED_MEMBER");
            String newAccessToken = jwtService.generateAccessToken(tokenCreateRequest);



            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + newAccessToken);

            return new ResponseEntity<>("accesstoken발급이 완료되었습니다.", headers, HttpStatus.OK);
        }

    }

}
