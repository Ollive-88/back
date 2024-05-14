package org.palpalmans.ollive_back.domain.board.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTagRequest {
    private Long id;
    private String name;
}
