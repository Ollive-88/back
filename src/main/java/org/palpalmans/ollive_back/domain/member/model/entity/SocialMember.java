package org.palpalmans.ollive_back.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.palpalmans.ollive_back.domain.member.model.status.SocialType;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember extends Member{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    public SocialMember(Member member, SocialType socialType) {
        super(member.getId(), member.getEmail(), member.getGender(), member.getBirthday(),
                member.getName(), member.getNickname(), member.getRole(), member.getProfilePicture());
        this.socialType = socialType;
    }
}
