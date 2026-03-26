package org.example.kursclassificator.dto.breedPropertyValue;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@Accessors(chain = true)
public class BreedPropertyValueGetDto {

    private Long breedId;

    private Long propertyId;

}
