package org.palpalmans.ollive_back.domain.board.model.dto.response;

public record DeleteImageRequest(
        Long id,
        String imageUrl
) {
}
