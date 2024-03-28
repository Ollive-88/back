package org.palpalmans.ollive_back.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.member.model.dto.request.JoinRequest;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.model.entity.SocialMember;
import org.palpalmans.ollive_back.domain.member.model.status.JoinRequestStatus;
import org.palpalmans.ollive_back.domain.member.model.status.MemberRole;
import org.palpalmans.ollive_back.domain.member.model.status.SocialType;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinRequestStatus joinProcess(JoinRequest joinRequest){

        String email = joinRequest.getEmail();
        String password = joinRequest.getPassword();
        String gender = joinRequest.getGender();
        Date birthday = joinRequest.getBirthday();
        String name = joinRequest.getName();
        MemberRole role;
        try {
            role = MemberRole.valueOf(joinRequest.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            // role 문자열이 MemberRole에 없는 경우 예외 처리
            return JoinRequestStatus.INVALID_ROLE;
        }



        Boolean isExist = memberRepository.existsByEmail(email);
        if(isExist) return JoinRequestStatus.EMAIL_DUPLICATED;

        //컬럼들이 notnull
        //정보를 무조건 가져와야하는건 일반회원가입멤버
        //소셜은 충분한 정보 없을 수 있음

        if(role == MemberRole.ROLE_NON_REGISTERED_MEMBER){
            //ROLE_NON_REGISTERD_MEMBER 면 SocialMember Entity 이용해서 회원가입
            Member member = Member.builder()
                    .email(email)
                    .gender("undefined")
                    .birthday(birthday)
                    .name(name)
                    .nickname("띠디딩딩")
                    .role(role) // role 설정
                    .profilePicture("src/main/java/org/palpalmans/ollive_back/domain/member/images/yuna.jpg")
                    .build();

            SocialMember joinMember = new SocialMember(member, SocialType.GOOGLE);

            memberRepository.save(joinMember);


        }else if(role == MemberRole.ROLE_REGISTERED_MEMBER){

            if(email == null || birthday == null || name == null){
                return JoinRequestStatus.NULL_EXIST;
            }
            //ROLE_REGISTERED_MEMBER 면 NormalMember Entity 이용해서 회원가입
            Member member = Member.builder()
                    .email(email)
                    .gender(gender)
                    .birthday(birthday)
                    .name(name)
                    .nickname("띠디딩딩")
                    .role(role) // role 설정
                    .profilePicture("src/main/java/org/palpalmans/ollive_back/domain/member/images/yuna.jpg")
                    .build();

            String encodedPass = bCryptPasswordEncoder.encode(password);

            NormalMember joinMember = new NormalMember(member, encodedPass);

            //멤버 가입시키기
            memberRepository.save(joinMember);

        }

        //모든 과정이 끝나면 성공 반환
        return JoinRequestStatus.JOIN_SUCCESS;

    }

}
