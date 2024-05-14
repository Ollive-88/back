package org.palpalmans.ollive_back.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.member.model.dto.response.MemberInfoResponse;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.status.MemberRole;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.common.security.service.JwtService;
import org.palpalmans.ollive_back.domain.member.service.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String atc= request.getHeader("Authorization");
        String rtc = request.getHeader("Refresh");
        log.info("atc is {}", atc);
        log.info("rtc is {}", rtc);

        if (StringUtils.hasText(rtc)){
            log.info("Refresh token detected");
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(atc)) {
            log.info("Authorization head is blank");
            filterChain.doFilter(request, response);
            return;
        }

        if (!atc.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }


        String token = atc.split(" ")[1];

        log.info("token = {} ", token);

        if (jwtService.isExpired(token)) {
            log.info("access token expired");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
            return;
        }

        long memberId = jwtService.getId(token);
        String memberRole = jwtService.getRole(token);

        MemberRole role = MemberRole.valueOf(memberRole);

        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);

        Member member = Member.builder()
                .id(memberId)
                .nickname(memberInfoResponse.getNickname())
                .email(memberInfoResponse.getEmail())
                .gender(memberInfoResponse.getGender())
                .birthday(memberInfoResponse.getBirthday())
                .role(role)
                .build();

        log.info("member.role ={}", member.getRole().name());

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        Authentication auth = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }
}

