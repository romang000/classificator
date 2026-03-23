package org.example.kursclassificator.mapper;

import org.example.kursclassificator.dto.breed.*;
import org.example.kursclassificator.entity.*;
import org.mapstruct.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BreedMapper {

    BreedResponse toDto(BreedEntity entity);

}
