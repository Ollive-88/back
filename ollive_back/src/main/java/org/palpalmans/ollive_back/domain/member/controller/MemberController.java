package org.palpalmans.ollive_back.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.member.model.dto.request.JoinRequest;
import org.palpalmans.ollive_back.domain.member.model.dto.request.ModifyMemberInfoRequest;
import org.palpalmans.ollive_back.domain.member.model.dto.response.MemberInfoResponse;
import org.palpalmans.ollive_back.domain.member.model.status.JoinRequestStatus;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.member.service.JoinService;
import org.palpalmans.ollive_back.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final JoinService joinService;
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest joinRequest){


        JoinRequestStatus status = joinService.joinProcess(joinRequest);


        return switch(status){
            case JOIN_SUCCESS -> ResponseEntity.status(HttpStatus.OK).body("회원가입이 성공적으로 완료되었습니다.");
            case NULL_EXIST -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 입력 항목이 누락되었습니다.");
            case EMAIL_DUPLICATED -> ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
            case INVALID_ROLE -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("유저 role이 잘못되었습니다");
        };
    }

    @GetMapping("/memberinfo")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal CustomMemberDetails customMemberDetails){

        long id = customMemberDetails.getId();

        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(id); //존재하지 않는 경우는 respository에서 예외처리

        return ResponseEntity.ok(memberInfoResponse);
    }

    @PatchMapping("/memberinfo")
    public ResponseEntity<String> modifyMemberInfo(@AuthenticationPrincipal CustomMemberDetails customMemberDetails, @RequestBody ModifyMemberInfoRequest modifyMemberInfoRequest){

        //현재 사용자 id값 가져오기
        long id = customMemberDetails.getId();
        String password = modifyMemberInfoRequest.getPassword();
        String gender = modifyMemberInfoRequest.getGender();
        String profilePicture = modifyMemberInfoRequest.getProfilePicture();
        String nickname = modifyMemberInfoRequest.getNickname();
        //멤버 서비스에서 주어진 값을 이용해 정보 업데이트
        log.info("id = {}", id);
        log.info("password = {}", password);
        log.info("gender = {}", gender);
        log.info("profilePicture = {}", profilePicture);
        log.info("nickname = {}", nickname);

        //todo : save 관련 오류 수정하기

        // 패스워드값이 있으면 normalmember 가져와서 수정
        if (password != null) {
            String Pass = bCryptPasswordEncoder.encode(password);
            Boolean isDone = memberService.modifyPassword(id, Pass);
            log.info("password isDone = {}", isDone);
        }

        // gender 값이 있으면 수정
        if (gender != null) {
            Boolean isDone = memberService.modifyGender(id, gender);
            log.info("gender isDone = {}", isDone);
        }

        // profilePicture 값이 있으면 수정
        if (profilePicture != null) {
            Boolean isDone = memberService.modifyProfilePicture(id, profilePicture);
            log.info("picture isDone = {}", isDone);
        }

        // nickname 값이 있으면 수정
        if (nickname != null) {
            Boolean isDone = memberService.modifyNickname(id, nickname);
            log.info("nickname isDone = {}", isDone);
        }


        //업데이트 완료되면 상태에 따라서 다른 값 반환



        return ResponseEntity.ok("정보 수정이 완료되었습니다");
    }


}
