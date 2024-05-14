package org.palpalmans.ollive_back.domain.cloth.respository;

import org.palpalmans.ollive_back.domain.cloth.model.entity.Cloth;
import org.palpalmans.ollive_back.domain.cloth.model.entity.ClothMember;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClothMemberRepository extends JpaRepository<ClothMember, Long> {
    Optional<ClothMember> findByClothAndMember(Cloth cloth, Member member);

    @Query(value = "SELECT * FROM cloth_member c WHERE c.id > :lastIndex AND c.member_id = :member ORDER BY c.created_at DESC LIMIT :sized", nativeQuery = true)
    List<ClothMember> findAllByMemberZeroIndex(int lastIndex, int sized, Long member);

    @Query(value = "SELECT * FROM cloth_member c WHERE c.id < :lastIndex AND c.member_id = :member ORDER BY c.created_at DESC LIMIT :sized", nativeQuery = true)
    List<ClothMember> findAllByMember(int lastIndex, int sized, Long member);
}
