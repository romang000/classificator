package org.example.kursclassificator.dto.breed;

import java.util.*;

import lombok.*;
import lombok.experimental.*;
import org.example.kursclassificator.dto.property.*;

@Data
@Builder
@Accessors(chain = true)
public class BreedUnfilledDto {

    Long breedId;

    String breedName;

    List<PropertyResponse> unfilledProperties;

}
