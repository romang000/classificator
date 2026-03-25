package org.example.kursclassificator.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("Ошибка сервера");

    private final String message;
}
