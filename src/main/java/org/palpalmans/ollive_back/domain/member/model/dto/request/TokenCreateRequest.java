package org.palpalmans.ollive_back.domain.member.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class TokenCreateRequest {

    private Long id;
    private String role;

}
