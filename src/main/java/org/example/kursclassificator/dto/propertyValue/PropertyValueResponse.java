package org.example.kursclassificator.dto.propertyValue;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class PropertyValueResponse {

    private Long id;

    private Long propertyId;

    private String value;

}
