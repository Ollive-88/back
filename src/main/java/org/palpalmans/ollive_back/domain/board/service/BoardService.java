package org.palpalmans.ollive_back.domain.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.board.model.dto.DeleteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.WriteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.request.UpdateBoardRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.request.WriteBoardRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.response.*;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.BoardTag;
import org.palpalmans.ollive_back.domain.board.model.entity.Tag;
import org.palpalmans.ollive_back.domain.board.repository.BoardQueryRepository;
import org.palpalmans.ollive_back.domain.board.repository.BoardRepository;
import org.palpalmans.ollive_back.domain.board.repository.BoardTagRepository;
import org.palpalmans.ollive_back.domain.image.model.dto.GetImageResponse;
import org.palpalmans.ollive_back.domain.image.service.ImageService;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.palpalmans.ollive_back.common.error.ErrorMessage.BOARD_NOT_FOUND;
import static org.palpalmans.ollive_back.common.error.ErrorMessage.NOT_AUTHORIZED;
import static org.palpalmans.ollive_back.domain.board.model.BoardMapper.toGetBoardDetailResponse;
import static org.palpalmans.ollive_back.domain.board.model.BoardMapper.toGetBoardResponse;
import static org.palpalmans.ollive_back.domain.image.model.ImageType.BOARD;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ImageService imageService;

    private final TagService tagService;
    private final LikeService likeService;
    private final ViewService viewService;
    private final CommentService commentService;

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final BoardTagRepository boardTagRepository;

    @Transactional
    public Long writeBoard(WriteBoardRequest writeBoardRequest, Member member) {
        Board board = writeBoard(
                writeBoardRequest.getTitle(), writeBoardRequest.getContent(), member
        );

        imageService.saveImage(writeBoardRequest.getImages(), BOARD, board.getId());

        List<Tag> beMappedTag = new ArrayList<>();
        tagService.saveTags(writeBoardRequest.getTagNames(), beMappedTag);
        boardTagRepository.saveAll(beMappedTag.stream().map(tag -> new BoardTag(board, tag)).toList());
        viewService.saveView(board, member);

        return board.getId();
    }

    @Transactional(readOnly = true)
    public GetBoardsResponse getBoards(
            String keyword, Long lastIndex, int size, Long memberId,
            List<String> tagNames
    ) {
        List<Board> boards = boardQueryRepository.getBoardList(keyword, lastIndex, size, memberId, tagNames);
        List<GetBoardResponse> getBoardResponseList = new ArrayList<>();
        for (Board board : boards) {
            int viewCount = viewService.getViewCount(board);
            int likeCount = likeService.getLikeCount(board);

            List<GetImageResponse> images = imageService.getImages(BOARD, board.getId());
            String thumbnail = images.isEmpty() ? "" : images.get(0).address();

            getBoardResponseList.add(toGetBoardResponse(board, viewCount, likeCount, thumbnail));
        }
        return new GetBoardsResponse(getBoardResponseList, boards.size() < size);
    }

    @Transactional(readOnly = true)
    public GetBoardDetailResponse getBoardDetail(Long boardId, CustomMemberDetails customMemberDetails) {
        Board board = getBoard(boardId);
        int viewCount = viewService.getViewCount(board);
        int likeCount = likeService.getLikeCount(board);
        boolean isLiked = likeService.isLikedMember(board, customMemberDetails.getMember());

        List<GetImageResponse> images = imageService.getImages(BOARD, boardId);
        List<GetTagResponse> tags = board.getBoardTags()
                .stream()
                .map(boardTag -> new GetTagResponse(
                        boardTag.getTag().getId(),
                        boardTag.getTag().getName()))
                .toList();

        return toGetBoardDetailResponse(
                board, customMemberDetails.getMember(),
                viewCount, likeCount, isLiked,
                images, tags
        );
    }

    @Transactional
    public void updateBoard(
            Long boardId,
            UpdateBoardRequest updateBoardRequest,
            CustomMemberDetails customMemberDetails
    ) {
        Board board = getBoard(boardId);
        if (board.getMember().getId() != customMemberDetails.getId()) {
            throw new AuthorizationServiceException(NOT_AUTHORIZED.getMessage());
        }

        board.changeTitle(updateBoardRequest.getTitle());
        board.changeContent(updateBoardRequest.getContent());
        boardRepository.save(board);

        List<Tag> savedTag = new ArrayList<>();
        tagService.saveTags(updateBoardRequest.getUpdateTagNames(), savedTag);
        boardTagRepository.saveAll(
                savedTag.stream()
                        .map(tag -> new BoardTag(board, tag))
                        .toList());

        updateBoardRequest.getDeleteTags()
                .forEach(deleteTagId ->
                        boardTagRepository.deleteBoardTagByBoardAndTag_Id(board, deleteTagId));

        imageService.saveImage(updateBoardRequest.getUpdateImages(), BOARD, boardId);
        imageService.deleteImages(updateBoardRequest.getDeleteImages());
    }

    @Transactional
    public void deleteBoard(Long boardId, CustomMemberDetails customMemberDetails) {
        Board board = getBoard(boardId);
        if (board.getMember().getId() != customMemberDetails.getId()) {
            throw new AuthorizationServiceException(NOT_AUTHORIZED.getMessage());
        }
        boardRepository.delete(board);
    }

    @Transactional
    public GetCommentResponse writeComment(WriteCommentRequest writeCommentRequest, Long boardId, Member member) {
        Board board = getBoard(boardId);
        return commentService.writeComment(writeCommentRequest, board, member);
    }

    @Transactional
    public void deleteComment(DeleteCommentRequest deleteCommentRequest, Member member) {
        commentService.deleteComment(deleteCommentRequest, member);
    }

    @Transactional
    public void toggleLike(Long boardId, Member member) {
        Board board = getBoard(boardId);
        likeService.findLikeByBoardAndMember(board, member)
                .ifPresentOrElse(likeService::deleteLike,
                        () -> likeService.saveLike(board, member));
    }

    private Board writeBoard(String title, String content, Member member) {
        return boardRepository.save(new Board(title, content, member));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(BOARD_NOT_FOUND.getMessage()));
    }
}
