package org.example.kursclassificator.dto.breedPropertyValue;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@Accessors(chain = true)
public class BreedPropertyValueResponse {

    private Long breedId;

    private Long propertyId;

    private List<Long> addedValues;

    private List<Long> skippedValues;

}
