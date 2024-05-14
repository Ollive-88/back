package org.palpalmans.ollive_back.domain.board.model.dto;

import org.jetbrains.annotations.NotNull;

public record DeleteCommentRequest(
        @NotNull
        Long commentId
) {
}
