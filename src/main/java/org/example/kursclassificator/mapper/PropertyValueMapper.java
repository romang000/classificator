package org.example.kursclassificator.mapper;

import org.example.kursclassificator.dto.propertyValue.*;
import org.example.kursclassificator.entity.*;
import org.mapstruct.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface PropertyValueMapper {

    @Mapping(source = "property.id", target = "propertyId")
    PropertyValueResponse toDto(PropertyValueEntity entity);

}
