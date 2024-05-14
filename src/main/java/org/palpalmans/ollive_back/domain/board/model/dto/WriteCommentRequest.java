package org.palpalmans.ollive_back.domain.board.model.dto;

import jakarta.validation.constraints.NotBlank;

public record WriteCommentRequest(
        @NotBlank
        String content
) {
}
