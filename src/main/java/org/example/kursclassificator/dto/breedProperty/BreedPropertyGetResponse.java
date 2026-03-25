package org.example.kursclassificator.dto.breedProperty;

import java.util.*;

import lombok.*;
import lombok.experimental.*;
import org.example.kursclassificator.dto.property.*;

@Data
@Accessors(chain = true)
public class BreedPropertyGetResponse {

    private Long breedId;

    private List<PropertyResponse> properties;

}
