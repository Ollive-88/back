package org.palpalmans.ollive_back.domain.board.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class WriteBoardRequest {
    @NotBlank(message = "title은 공백일 수 없습니다.")
    private final String title;
    @NotBlank(message = "content는 공백일 수 없습니다.")
    private final String content;

    @Setter
    private List<String> tagNames = new ArrayList<>();

    @Setter
    private List<MultipartFile> images = new ArrayList<>();
}
