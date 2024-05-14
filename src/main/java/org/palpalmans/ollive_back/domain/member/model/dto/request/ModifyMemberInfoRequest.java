package org.palpalmans.ollive_back.domain.member.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
public class ModifyMemberInfoRequest {
    private String password;
    private String gender;
    private String nickname;
}
