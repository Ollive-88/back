package org.palpalmans.ollive_back.domain.member.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.palpalmans.ollive_back.common.BaseTimeEntity;
import org.palpalmans.ollive_back.domain.member.model.status.MemberRole;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@ToString
@Setter(value = PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Setter
    @Column
    private String profilePicture;

    @Builder
    public Member(long id, String email, String gender, Date birthday, String name,
                  String nickname, MemberRole role, String profilePicture){
        this.id = id;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public void changeGender(String newGender){
        this.gender = newGender;
    }

    public void changeNickname(String newNickname){
        this.nickname = newNickname;
    }

}
