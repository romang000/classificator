package org.example.kursclassificator.dto.breedProperty;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@Accessors(chain = true)
public class BreedPropertyResponse {

    private Long breedId;

    private List<Long> addedProperties;

    private List<Long> skippedProperties;

}
