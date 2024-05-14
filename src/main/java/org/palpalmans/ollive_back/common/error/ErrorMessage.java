package org.palpalmans.ollive_back.common.error;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOT_BLANK("공백일 수 없습니다."),
    BOARD_NOT_FOUND("해당하는 게시글이 존재하지 않습니다."),
    IMAGE_FILE_NOT_SAVED("이미지 파일이 저장되지 않았습니다."),
    IMAGE_ENTITY_NOT_SAVED("이미지 데이터가 저장되지 않았습니다."),
    IMAGE_FILE_NOT_DELETED("이미지 파일이 삭제되지 않았습니다."),
    IMAGE_ENTITY_NOT_DELETED("이미지 데이터가 삭제되지 않았습니다."),
    NOT_AUTHORIZED("권한이 없습니다."),
    UNKNOWN("알 수 없는 에러가 발생하였습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
