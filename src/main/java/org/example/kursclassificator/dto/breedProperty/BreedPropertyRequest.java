package org.example.kursclassificator.dto.breedProperty;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedPropertyRequest {

    private Long breedId;

    private List<Long> propertyIds;

}
