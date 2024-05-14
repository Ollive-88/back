package org.palpalmans.ollive_back.domain.board.model;

import org.palpalmans.ollive_back.domain.board.model.dto.request.WriteBoardRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.response.*;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.image.model.dto.GetImageResponse;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;

import java.util.List;

import static java.util.Comparator.comparing;
import static org.palpalmans.ollive_back.domain.board.model.CommentMapper.toGetCommentResponse;

public class BoardMapper {
    public static Board toBoard(WriteBoardRequest writeBoardRequest, Member member) {
        return new Board(
                writeBoardRequest.getTitle(), writeBoardRequest.getContent(), member
        );
    }

    public static WriteBoardResponse toWriteBoardResponse(Long boardId) {
        return new WriteBoardResponse(boardId);
    }

    public static GetBoardResponse toGetBoardResponse(
            Board board, int views, int likes, String thumbnail
    ) {
        return GetBoardResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .thumbnailAddress(thumbnail)
                .commentCount(board.getComments().size())
                .likes(likes)
                .views(views)
                .createdAt(board.getCreatedAt())
                .tags(board.getBoardTags()
                        .stream()
                        .map(boardTag ->
                                boardTag.getTag()
                                        .getName())
                        .toList())
                .build();
    }

    public static GetBoardDetailResponse toGetBoardDetailResponse(
            Board board, Member loginMember,
            int viewCount, int likeCount, boolean isLiked,
            List<GetImageResponse> images, List<GetTagResponse> tags
    ) {
        Member writer = board.getMember();
        return GetBoardDetailResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .isLiked(isLiked)
                .writer(new Writer(writer.getNickname(), writer.getProfilePicture()))
                .isMine(writer.getId() == loginMember.getId())
                .createdAt(board.getCreatedAt())
                .viewCount(viewCount)
                .likeCount(likeCount)
                .images(images)
                .tags(tags)
                .comments(board.getComments()
                        .stream()
                        .map(comment -> toGetCommentResponse(comment, loginMember))
                        .sorted(comparing(GetCommentResponse::createdAt))
                        .toList())
                .build();
    }
}
