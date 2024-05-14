package org.palpalmans.ollive_back.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NormalMember extends Member{

    @Column(nullable = false)
    private String password;

    public NormalMember(Member member, String password) {
        super(member.getId(), member.getEmail(), member.getGender(), member.getBirthday(),
                member.getName(), member.getNickname(), member.getRole(), member.getProfilePicture());
        this.password = password;
    }

}
