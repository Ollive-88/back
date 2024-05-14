package org.palpalmans.ollive_back.domain.board.repository;

import org.palpalmans.ollive_back.domain.board.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
