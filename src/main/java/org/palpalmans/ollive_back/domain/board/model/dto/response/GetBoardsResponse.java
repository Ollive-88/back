package org.palpalmans.ollive_back.domain.board.model.dto.response;

import java.util.List;

public record GetBoardsResponse(
        List<GetBoardResponse> boards,
        boolean isEnd
) {
}
