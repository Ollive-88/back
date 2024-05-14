package org.palpalmans.ollive_back.domain.member.controller;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palpalmans.ollive_back.domain.member.model.dto.request.JoinRequest;
import org.palpalmans.ollive_back.common.security.service.JwtService;
import org.palpalmans.ollive_back.domain.member.service.JoinService;
import org.palpalmans.ollive_back.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest //웹 서버를 별도로 띄우지 않고도 스프링 MVC 동작을 재현
@AutoConfigureMockMvc //MockMvc 인스턴스 자동 생성
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JoinService joinService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtService jwtService;


    @Test
    @DisplayName("Join member controller test")
    @Transactional
    void joinMember() throws Exception{
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail("test@test.com");
        joinRequest.setName("testUser");
        joinRequest.setGender("Male");
        joinRequest.setBirthday(new Date());
    }
}
