package org.palpalmans.ollive_back.domain.member.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class MemberInfoResponse {

    private String email;
    private String gender;
    private Date birthday;
    private String name;
    private String nickname;
    private String profilePicture;

}
