package org.palpalmans.ollive_back.domain.member.model.dto.response;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.palpalmans.ollive_back.domain.member.model.dto.response.Oauth2MemberResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@ToString
public class CustomOauth2User implements OAuth2User {

    private final Oauth2MemberResponse oauth2MemberResponse;

    @Override
    //일반적으로 사용자의 속성(예: 이름, 이메일 등)을 Map 형태로 반환
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    //권한 관리
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return oauth2MemberResponse.getName();
    }

    public String getEmail(){
        return oauth2MemberResponse.getEmail();
    }
}
