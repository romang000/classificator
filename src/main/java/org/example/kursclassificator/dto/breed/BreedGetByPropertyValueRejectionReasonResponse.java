package org.example.kursclassificator.dto.breed;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedGetByPropertyValueRejectionReasonResponse {

    private String propertyName;

    private String actualValue;

    private String expectedValue;

    private String reason;

}
