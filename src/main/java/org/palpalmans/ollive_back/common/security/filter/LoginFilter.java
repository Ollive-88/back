package org.palpalmans.ollive_back.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.common.security.service.JwtService;
import org.palpalmans.ollive_back.domain.member.model.dto.GeneratedToken;
import org.palpalmans.ollive_back.domain.member.model.dto.request.TokenCreateRequest;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private static final String SPRING_SECURITY_FORM_EMAIL_KEY = "email";

    @Nullable
    protected String obtainEmail(HttpServletRequest request) {
        //새로운 파라미터 생성
        return request.getParameter(SPRING_SECURITY_FORM_EMAIL_KEY);
    }

    public LoginFilter(AuthenticationManager authenticationManager, JwtService jwtService){

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 memberLoginId, memberPassword 추출
        String email = obtainEmail(request);
        String password = obtainPassword(request);

        log.info("LoginFilter.attemptAuthentication() email = {}", email);
        log.info("LoginFilter.attemptAuthentication() password = {}", password);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        log.info("authToken = {}",authToken);

        return authenticationManager.authenticate(authToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

        long id = customMemberDetails.getId();

        //role 값 뽑아내기
        log.info("LoginFilter.successfulAuthentication() authentication = {}", authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("LoginFilter.successfulAuthentication() authorities = {}", authorities.toString());
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        log.info("role = {}", role);

//        토큰생성 access+refresh
        TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();
        tokenCreateRequest.setId(id);
        tokenCreateRequest.setRole(role);
        log.info("LoginFilter.TokenCreateRequest.Id = {}", id);


        GeneratedToken token = jwtService.generateToken(tokenCreateRequest);

        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();
        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("Refresh", refreshToken));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(409);
        try {
            response.getWriter().write("로그인이 실패했습니다.");
        } catch (IOException e) {
            logger.error("Error writing to response", e);
        }
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
