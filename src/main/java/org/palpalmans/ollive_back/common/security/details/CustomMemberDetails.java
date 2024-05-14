package org.palpalmans.ollive_back.common.security.details;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
@ToString
public class CustomMemberDetails implements UserDetails {
    private final Member member;

    public CustomMemberDetails(Member member){
        this.member = member;
    }

    //유저 권한 가져오기
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();

        log.debug("login user role = {}", member.getRole());
        authorityList.add(new SimpleGrantedAuthority(member.getRole().name()));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return ((NormalMember) member).getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    public long getId(){ return  member.getId(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
