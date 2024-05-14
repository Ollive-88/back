package org.palpalmans.ollive_back.domain.ingredient.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UtilMethods {
    public static boolean isValidLocalDate(String dateTimeStr) {
        String pattern = "yyyy-MM-dd";

        try {
            // 문자열을 LocalDateTime으로 파싱 시도
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(dateTimeStr, formatter);
            return true; // 파싱 성공 시 true 반환
        } catch (DateTimeParseException e) {
            return false; // 파싱 실패(예외 발생) 시 false 반환
        }
    }
}