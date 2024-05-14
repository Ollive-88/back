package org.palpalmans.ollive_back.common.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.common.security.handler.CustomSuccessHandler;
import org.palpalmans.ollive_back.common.security.filter.LoginFilter;
import org.palpalmans.ollive_back.common.security.service.JwtService;
import org.palpalmans.ollive_back.common.security.service.CustomOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOauth2UserService customOAuth2Service;
    private final CustomSuccessHandler customSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        List<String> exposedHeaders = new ArrayList<>();
        exposedHeaders.add("Authorization");
        exposedHeaders.add("Refresh");

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); //허용할 포트 번호
//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L); //허용을 유지할 시간


//                        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh"));
//                        configuration.addExposedHeader("Authorization");
//                        configuration.addExposedHeader("Refresh");
//                        configuration.addAllowedHeader("Refresh");
                        configuration.setExposedHeaders(exposedHeaders); //Authorization 헤더도 허용
//                        configuration.setExposedHeaders(Collections.singletonList("Refresh")); //Refresh 헤더도 허용
                        return configuration;
                    }
                })));

        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        //Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        //세션 stateless 상태로 설정
        http
                //세션이 있어도 사용하지 않고, 없어도 만들지 않음
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2Service))
                        .successHandler(customSuccessHandler)
                );

        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtService), UsernamePasswordAuthenticationFilter.class);

        //인가작업
        http
                .authorizeHttpRequests((auth) -> auth
                        // 모든 경로("/", "/login", "/join", "/member/**", "/swagger-ui/**", "/v3/api-docs/**", "/oauth2/**")에 대한 접근 허용
                        .requestMatchers("/**","/login/**","/login","/","/join", "/member/**", "/swagger-ui/**", "/v3/api-docs/**","/oauth2/**").permitAll()
                        // "/admin" 경로에 대한 접근은 "ADMIN" 역할을 가진 사용자만 허용
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 그 외의 모든 요청은 인증된 사용자에게만 허용
                        .requestMatchers("/tokentest").hasRole("REGISTERED_MEMBER")
                        .anyRequest().authenticated());

        return http.build();

    }


}
