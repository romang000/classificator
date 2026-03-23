package org.example.kursclassificator.dto.breed;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedResponse {

    private Long id;

    private String name;

}
