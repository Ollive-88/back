package org.palpalmans.ollive_back.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.board.model.dto.DeleteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.WriteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.response.GetCommentResponse;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.Comment;
import org.palpalmans.ollive_back.domain.board.repository.CommentRepository;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.palpalmans.ollive_back.domain.board.model.CommentMapper.toComment;
import static org.palpalmans.ollive_back.domain.board.model.CommentMapper.toGetCommentResponse;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public GetCommentResponse writeComment(
            WriteCommentRequest writeCommentRequest,
            Board board,
            Member member
    ) {
        Comment comment = commentRepository.save(toComment(writeCommentRequest, board, member));
        return toGetCommentResponse(comment, comment.getMember());
    }

    @Transactional
    public void deleteComment(DeleteCommentRequest deleteCommentRequest, Member member) {
        commentRepository.deleteById(deleteCommentRequest.commentId());
    }
}
