package org.palpalmans.ollive_back.domain.member.repository;

import jakarta.transaction.Transactional;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.model.entity.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> { // JPA 레포지토리 상속받고, 엔티티와 id의 레퍼런스 타입 입력받기

    Boolean existsByEmail(String email);
    Optional<Member> getMemberById(long id);

    Optional<Member> getMemberByEmail(String email);

    @Transactional
    Integer deleteMemberById(long id);

    @Query("SELECT n FROM NormalMember n WHERE n.email = :email")
    Optional<NormalMember> getNormalMemberByEmail(@Param("email") String email);

    @Query("SELECT n FROM NormalMember n WHERE n.id = :id")
    Optional<NormalMember> getNormalMemberById(@Param("id") long id);

    @Query("SELECT s FROM SocialMember s WHERE s.email = :email")
    Optional<SocialMember> getSocialMemberByEmail(@Param("email") String email);


}
