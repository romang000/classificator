package org.example.kursclassificator.exception;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ClassificatorExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        var dto = new ErrorDto(
            ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );

        var resp = ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(dto);

        return resp;

    }

    @ExceptionHandler(ClassificatorException.class)
    public ResponseEntity<ErrorDto> handleBestException(ClassificatorException ex) {
        log.warn("Application exception: {}", ex.getMessage(), ex);
        var dto = new ErrorDto(
            ex.getMessage()
        );

        var resp = ResponseEntity
            .status(ex.getStatus())
            .body(dto);

        return resp;

    }
}
