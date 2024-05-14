package org.palpalmans.ollive_back.domain.board.model.dto.response;

import lombok.Builder;
import org.palpalmans.ollive_back.domain.image.model.dto.GetImageResponse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetBoardDetailResponse(
        Long boardId,
        String title,
        String content,
        int viewCount,
        int likeCount,
        boolean isMine,
        boolean isLiked,
        Writer writer,
        List<GetImageResponse> images,
        List<GetTagResponse> tags,
        LocalDateTime createdAt,
        List<GetCommentResponse> comments
) {
}
