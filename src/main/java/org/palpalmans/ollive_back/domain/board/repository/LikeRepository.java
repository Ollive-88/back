package org.palpalmans.ollive_back.domain.board.repository;

import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.Like;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    int countLikesByBoard(Board board);

    boolean existsLikeByBoardAndMember(Board board, Member member);

    Optional<Like> findByBoardAndMember(Board board, Member member);
}
