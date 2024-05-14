package org.palpalmans.ollive_back.domain.member.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Oauth2MemberResponse {
    private String name;
    private String email;
}
