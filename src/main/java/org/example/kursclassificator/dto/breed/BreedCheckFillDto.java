package org.example.kursclassificator.dto.breed;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Builder
@Accessors(chain = true)
public class BreedCheckFillDto {

    List<BreedUnfilledDto> breeds;

    boolean allFilled;

}
