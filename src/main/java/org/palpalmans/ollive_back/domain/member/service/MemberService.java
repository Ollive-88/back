package org.palpalmans.ollive_back.domain.member.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.image.model.ImageType;
import org.palpalmans.ollive_back.domain.image.model.dto.GetImageResponse;
import org.palpalmans.ollive_back.domain.image.service.ImageService;
import org.palpalmans.ollive_back.domain.member.model.dto.response.MemberInfoResponse;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.model.entity.SocialMember;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(long id) {

        Member member = memberRepository.getMemberById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID를 가진 멤버가 존재하지 않습니다: " + id));

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        memberInfoResponse.setNickname(member.getNickname());
        memberInfoResponse.setName(member.getName());
        memberInfoResponse.setGender(member.getGender());
        memberInfoResponse.setEmail(member.getEmail());
        memberInfoResponse.setBirthday(member.getBirthday());
        memberInfoResponse.setProfilePicture(member.getProfilePicture());


        return memberInfoResponse;
    }

    //이메일로 정보 불러오기
    @Transactional(readOnly = true)
    public Optional<Member> getMemberInfo(String email) {

        return memberRepository.getMemberByEmail(email);
    }

    //이메일로 패스워드 포함 멤버 불러오기
    @Transactional(readOnly = true)
    public Optional<NormalMember> getNormalMemberByEmail(String email) {
        return memberRepository.getNormalMemberByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<SocialMember> getSocialMemberByEmail(String email) {
        return memberRepository.getSocialMemberByEmail(email);
    }

    @Transactional
    public Boolean modifyPassword(long id, String password) {

        Optional<NormalMember> nm = memberRepository.getNormalMemberById(id);

        if (nm.isPresent()) {
            nm.get().setPassword(password);
            memberRepository.save(nm.get());
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean modifyGender(long id, String gender) {

        Optional<Member> member = memberRepository.getMemberById(id);

        if (member.isPresent()) {
            Member now = member.get();
            now.changeGender(gender);
            memberRepository.save(now);
            return true;
        }
        return false;
    }

    @Transactional
    public String modifyProfilePicture(long id, List<MultipartFile> profilePicture) {
        Member now = memberRepository.getMemberById(id)
                .orElseThrow(EntityExistsException::new);

        if (!now.getProfilePicture().isBlank()) {
            List<GetImageResponse> images = imageService.getImages(ImageType.PROFILE_PICTURE, now.getId());
            imageService.deleteImages(images.stream()
                    .map(GetImageResponse::id)
                    .toList());
        }

        if (profilePicture == null) {
            return "";
        }

        //받은 이미지 저장
        imageService.saveImage(profilePicture, ImageType.PROFILE_PICTURE, id);
        //저장한 이미지 정보 불러오기
        List<GetImageResponse> images = imageService.getImages(ImageType.PROFILE_PICTURE, id);
        String profile = images.isEmpty() ? "" : images.get(0).address();
        log.info("profile url = {}", profile);
        //이미지 url member에 넣기
        now.setProfilePicture(profile);
        //멤버 정보 수정하기
        memberRepository.save(now);
        return profile;
    }

    @Transactional
    public Boolean modifyNickname(long id, String nickname) {

        Optional<Member> member = memberRepository.getMemberById(id);

        if (member.isPresent()) {
            Member now = member.get();
            now.changeNickname(nickname);
            memberRepository.save(now);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean deleteMember(long id) {
        int a = memberRepository.deleteMemberById(id);
        return a == 1;
    }
}
