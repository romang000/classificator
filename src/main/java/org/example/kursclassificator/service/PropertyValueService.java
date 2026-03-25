package org.example.kursclassificator.service;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class PropertyValueService {

    private final PropertyValueRepository propertyValueRepository;

    private final PropertyRepository propertyRepository;

    public PropertyValueEntity create(Long propertyId, String value) {
        var isPropertyValueAlreadyExists = propertyValueRepository
            .existsByPropertyIdAndValue(
                propertyId,
                value
            );
        if (isPropertyValueAlreadyExists) {
            throw new ClassificatorException(
                "Значение для свойства уже существует",
                HttpStatus.CONFLICT
            );
        }

        var property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new ClassificatorException(
                "Свойство не найдено",
                HttpStatus.NOT_FOUND
            ));

        var propertyValue = PropertyValueEntity.builder()
            .value(value)
            .property(property)
            .build();

        var savedPropertyValue = propertyValueRepository.save(propertyValue);

        return savedPropertyValue;
    }

    public List<PropertyValueEntity> getAll() {
        var properties = propertyValueRepository.findAll();
        return properties;
    }

    public List<PropertyValueEntity> getByPropertyId(Long id) {
        var property = propertyRepository.findById(id)
            .orElseThrow(() -> new ClassificatorException(
                "Свойство не найдено",
                HttpStatus.NOT_FOUND
            ));

        var propertyValue = propertyValueRepository.findByPropertyId(id);
        return propertyValue;
    }

    public void delete(Long id) {
        var propertyValue = propertyValueRepository.findById(id)
            .orElseThrow(() -> new ClassificatorException(
                "Значение для свойства не найдено",
                HttpStatus.NOT_FOUND
            ));

        propertyValueRepository.delete(propertyValue);
    }

}
