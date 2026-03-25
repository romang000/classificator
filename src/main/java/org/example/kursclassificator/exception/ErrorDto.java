package org.example.kursclassificator.exception;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с ошибкой",
    requiredProperties = {
        "message"
    }
)
public class ErrorDto {

    @Schema(
        description = "Сообщение об ошибке",
        example = "Необходимо указать хотя бы один из параметров: officeId, employeeId"
    )
    private String message;

}