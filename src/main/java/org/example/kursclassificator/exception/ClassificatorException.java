package org.example.kursclassificator.exception;

import lombok.*;
import org.springframework.http.*;

@Getter
public class ClassificatorException extends RuntimeException {

    private final HttpStatus status;

    public ClassificatorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
