package org.example.kursclassificator.dto.propertyValue;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class PropertyValueRequest {

    private Long propertyId;

    private String value;

}
