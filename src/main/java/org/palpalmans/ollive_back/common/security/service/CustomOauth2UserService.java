package org.palpalmans.ollive_back.common.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.member.model.dto.request.JoinRequest;
import org.palpalmans.ollive_back.domain.member.model.dto.response.CustomOauth2User;
import org.palpalmans.ollive_back.domain.member.model.dto.response.GoogleResponse;
import org.palpalmans.ollive_back.domain.member.model.dto.response.Oauth2MemberResponse;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.SocialMember;
import org.palpalmans.ollive_back.domain.member.model.status.MemberRole;
import org.palpalmans.ollive_back.domain.member.model.status.SocialType;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.palpalmans.ollive_back.domain.member.service.MemberService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("oAuth2User = {}",oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        GoogleResponse googleResponse = null;

        if (registrationId.equals("google")) {

            googleResponse = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        //받은 멤버 정보를 객체에 담아 반환
        Oauth2MemberResponse oauth2MemberResponse = new Oauth2MemberResponse();

        //필요한 정보인 이름과 Email만 추출
        oauth2MemberResponse.setName(googleResponse.getName());
        oauth2MemberResponse.setEmail(googleResponse.getEmail());

        log.info("name = {}", googleResponse.getName());
        log.info("email = {}", googleResponse.getEmail());

        //DB에서 유저 정보 확인
        Optional<Member> isExist = memberService.getMemberInfo(googleResponse.getEmail());
        if(isExist.isEmpty()){
            // 유저 정보가 없다면 ROLE_NON_REGISTERED_MEMBER 회원가입 진행
            String nickname = googleResponse.getProviderId();
            String email = googleResponse.getEmail();
            String name = googleResponse.getName();
            String picture = googleResponse.getPicture();

            // Builder 패턴 사용하여 객체 생성
            Member member = Member.builder()
                    .email(email)
                    .name(name)
                    .gender("Male")
                    .nickname(nickname)
                    .birthday(new Date())
                    .role(MemberRole.ROLE_NON_REGISTERED_MEMBER) // role 설정
                    .build();

            SocialMember joinMember = new SocialMember(member, SocialType.GOOGLE);

            memberRepository.save(joinMember);


        }

        return new CustomOauth2User(oauth2MemberResponse);
    }
}
