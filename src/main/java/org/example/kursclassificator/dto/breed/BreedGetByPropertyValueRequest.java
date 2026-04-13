package org.example.kursclassificator.dto.breed;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedGetByPropertyValueRequest {

    private Long propertyId;

    private Long valueId;

}
