package org.example.kursclassificator.dto.breed;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedGetByPropertyValueDto {

    private Long propertyId;

    private Long valueId;

}
