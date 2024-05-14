package org.palpalmans.ollive_back.domain.member.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
public class JoinRequest {

    @NotBlank(message = "email은 공백일 수 없습니다")
    private String email;
    @NotBlank(message = "password는 공백일 수 없습니다")
    private String password;
    @NotBlank(message = "name은 공백일 수 없습니다.")
    private String name;
    private Date birthday;
    private String gender;
}
