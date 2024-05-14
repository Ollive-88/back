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
public class UpdateBoardRequest {
    @NotBlank
    private final String title;
    @NotBlank
    private final String content;
    @Setter
    private List<String> updateTagNames = new ArrayList<>();
    @Setter
    private List<Long> deleteTags = new ArrayList<>();
    @Setter
    private List<MultipartFile> updateImages = new ArrayList<>();
    @Setter
    private List<Long> deleteImages = new ArrayList<>();
}


/*
updateTagNames 추가된 태그
deleteTagNames 삭제된 태그
updateImages 추가된 이미지
deleteImages 삭제된 이미지
title 제목
content 본문
 */