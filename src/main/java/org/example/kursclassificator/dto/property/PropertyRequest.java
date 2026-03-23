package org.example.kursclassificator.dto.property;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class PropertyRequest {

    private String name;

}
