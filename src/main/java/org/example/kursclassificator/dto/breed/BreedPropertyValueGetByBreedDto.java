package org.example.kursclassificator.dto.breed;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedPropertyValueGetByBreedDto {

    public String propertyName;

    public String propertyValueName;

}
