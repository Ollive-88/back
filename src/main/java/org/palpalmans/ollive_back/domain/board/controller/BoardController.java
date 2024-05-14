package org.palpalmans.ollive_back.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.common.security.details.CustomMemberDetails;
import org.palpalmans.ollive_back.domain.board.model.dto.DeleteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.WriteCommentRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.request.UpdateBoardRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.request.WriteBoardRequest;
import org.palpalmans.ollive_back.domain.board.model.dto.response.GetBoardDetailResponse;
import org.palpalmans.ollive_back.domain.board.model.dto.response.GetBoardsResponse;
import org.palpalmans.ollive_back.domain.board.model.dto.response.GetCommentResponse;
import org.palpalmans.ollive_back.domain.board.model.dto.response.WriteBoardResponse;
import org.palpalmans.ollive_back.domain.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.palpalmans.ollive_back.domain.board.model.BoardMapper.toWriteBoardResponse;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<WriteBoardResponse> writeBoard(
            @Validated
            WriteBoardRequest writeBoardRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Long boardId = boardService.writeBoard(
                writeBoardRequest, customMemberDetails.getMember()
        );
        return ResponseEntity.status(CREATED)
                .body(toWriteBoardResponse(boardId));
    }

    @GetMapping
    public ResponseEntity<GetBoardsResponse> getBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") Long lastIndex,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "false") boolean isMyView,
            @RequestParam(required = false) List<String> tags,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        if (keyword != null && keyword.isBlank())
            throw new IllegalArgumentException("keyword는 공백일 수 없습니다!");
        if (tags == null)
            tags = Collections.emptyList();

        Long memberId = 0L;
        if (isMyView) {
            memberId = customMemberDetails.getMember().getId();
        }
        return ResponseEntity.ok(boardService.getBoards(keyword, lastIndex, size, memberId, tags));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardDetailResponse> getBoardDetail(
            @PathVariable(value = "boardId") Long boardId,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        return ResponseEntity.ok(boardService.getBoardDetail(boardId, customMemberDetails));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(
            @PathVariable(name = "boardId") Long boardId,
            @Validated UpdateBoardRequest updateBoardRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.updateBoard(boardId, updateBoardRequest, customMemberDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable(name = "boardId") Long boardId,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.deleteBoard(boardId, customMemberDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<GetCommentResponse> createComment(
            @PathVariable(name = "boardId") Long boardId,
            @Validated @RequestBody WriteCommentRequest writeCommentRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        GetCommentResponse getCommentResponse =
                boardService.writeComment(writeCommentRequest, boardId, customMemberDetails.getMember());
        return ResponseEntity.status(CREATED).body(getCommentResponse);
    }

    @DeleteMapping("/{boardId}/comments")
    public ResponseEntity<WriteBoardResponse> deleteComment(
            @Validated @RequestBody
            DeleteCommentRequest deleteCommentRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.deleteComment(deleteCommentRequest, customMemberDetails.getMember());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<WriteBoardResponse> toggleLike(
            @PathVariable(name = "boardId") Long boardId,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.toggleLike(boardId, customMemberDetails.getMember());
        return ResponseEntity.ok().build();
    }
}
