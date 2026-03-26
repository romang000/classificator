package org.example.kursclassificator.dto.property;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@Accessors(chain = true)
public class PropertyResponse {

    private Long id;

    private String name;

}
