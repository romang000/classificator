package org.example.kursclassificator.dto.breedPropertyValue;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@Accessors(chain = true)
public class BreedPropertyValueDeleteDto {

    private Long breedId;

    private Long propertyId;

    private List<Long> deleteValues;

    private List<Long> skippedValues;

}
