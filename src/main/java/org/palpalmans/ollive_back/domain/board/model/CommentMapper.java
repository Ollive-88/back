package org.palpalmans.ollive_back.domain.board.model;

import org.palpalmans.ollive_back.domain.board.model.dto.WriteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.response.GetCommentResponse;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.Comment;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;

public class CommentMapper {
    public static Comment toComment(WriteCommentRequest writeCommentRequest, Board board, Member member) {
        return new Comment(writeCommentRequest.content(), board, member);
    }

    public static GetCommentResponse toGetCommentResponse(
            Comment comment, Member loginMember
    ) {
        return GetCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .memberId(comment.getMember().getId())
                .isMine(loginMember.getId() == comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .memberProfile(comment.getMember().getProfilePicture())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
