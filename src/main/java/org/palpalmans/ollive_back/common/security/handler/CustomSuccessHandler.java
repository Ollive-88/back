package org.palpalmans.ollive_back.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.common.security.service.JwtService;
import org.palpalmans.ollive_back.domain.member.model.dto.GeneratedToken;
import org.palpalmans.ollive_back.domain.member.model.dto.request.TokenCreateRequest;
import org.palpalmans.ollive_back.domain.member.model.dto.response.CustomOauth2User;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.service.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOauth2User customOauth2User = (CustomOauth2User) authentication.getPrincipal();

        log.info("received information = {}", customOauth2User);
        Optional<Member> isExist = memberService.getMemberInfo(customOauth2User.getEmail());

        if (isExist.isPresent()) {
            Member member = isExist.get();
            Member social = Member.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .gender(member.getGender())
                    .birthday(member.getBirthday())
                    .role(member.getRole())
                    .build();

            CustomMemberDetails customMemberDetails = new CustomMemberDetails(social);

            Authentication auth = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);

            //토큰도 발급
            TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();

            tokenCreateRequest.setId(member.getId());
            tokenCreateRequest.setRole(String.valueOf(member.getRole()));

            GeneratedToken generatedToken = jwtService.generateToken(tokenCreateRequest);
            String accessToken = generatedToken.getAccessToken();
            String refreshToken = generatedToken.getRefreshToken();


            response.addHeader("Authorization", "Bearer " + accessToken);
            response.addCookie(createCookie("Refresh", refreshToken));

        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
