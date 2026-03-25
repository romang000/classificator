package org.example.kursclassificator.service;

import java.util.*;
import java.util.stream.*;

import lombok.*;
import org.example.kursclassificator.dto.breedPropertyValue.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class BreedPropertyValueService {

    private final BreedRepository breedRepository;

    private final PropertyRepository propertyRepository;

    private final PropertyValueRepository propertyValueRepository;

    private final BreedPropertyValueRepository breedPropertyValueRepository;

    public BreedPropertyValueResponse addValueToBreedProperty(BreedPropertyValueRequest request) {
        var breed = breedRepository.findById(request.getBreedId())
            .orElseThrow(() -> new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND
            ));

        var property = propertyRepository.findById(request.getPropertyId())
            .orElseThrow(() -> new ClassificatorException(
                "Свойство не найдено",
                HttpStatus.NOT_FOUND
            ));

        if (request.getPropertyValueIds() == null || request.getPropertyValueIds().isEmpty()) {
            throw new ClassificatorException(
                "Список значений свойства пуст",
                HttpStatus.BAD_REQUEST
            );
        }

        Set<Long> requestedValueIds = new LinkedHashSet<>(request.getPropertyValueIds());

        var propertyValues = propertyValueRepository.findAllById(requestedValueIds);

        if (propertyValues.size() != requestedValueIds.size()) {
            Set<Long> foundIds = propertyValues.stream()
                .map(PropertyValueEntity::getId)
                .collect(Collectors.toSet());

            List<Long> notFoundIds = requestedValueIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

            throw new ClassificatorException(
                "Значения свойства не найдены: " + notFoundIds,
                HttpStatus.NOT_FOUND
            );
        }

        List<Long> invalidPropertyValueIds = propertyValues.stream()
            .filter(value -> !value.getProperty().getId().equals(property.getId()))
            .map(PropertyValueEntity::getId)
            .toList();

        if (!invalidPropertyValueIds.isEmpty()) {
            throw new ClassificatorException(
                "Значения не принадлежат свойству " + property.getId() + ": " + invalidPropertyValueIds,
                HttpStatus.CONFLICT
            );
        }

        List<BreedPropertyValueEntity> existingRelations =
            breedPropertyValueRepository.findByBreedIdAndPropertyIdAndPropertyValueIdIn(
                breed.getId(),
                property.getId(),
                new ArrayList<>(requestedValueIds)
            );

        Set<Long> existingValueIds = existingRelations.stream()
            .map(rel -> rel.getPropertyValue().getId())
            .collect(Collectors.toSet());

        List<BreedPropertyValueEntity> newRelations = propertyValues.stream()
            .filter(value -> !existingValueIds.contains(value.getId()))
            .map(value -> BreedPropertyValueEntity.builder()
                .breed(breed)
                .property(property)
                .propertyValue(value)
                .build())
            .toList();

        if (!newRelations.isEmpty()) {
            breedPropertyValueRepository.saveAll(newRelations);
        }

        List<Long> addedValues = newRelations.stream()
            .map(rel -> rel.getPropertyValue().getId())
            .toList();

        return BreedPropertyValueResponse.builder()
            .breedId(breed.getId())
            .propertyId(property.getId())
            .addedValues(addedValues)
            .skippedValues(new ArrayList<>(existingValueIds))
            .build();
    }

    public BreedPropertyValueDeleteDto removeValueFromBreedProperty(BreedPropertyValueRequest request) {

        BreedEntity breed = breedRepository.findById(request.getBreedId())
            .orElseThrow(() -> new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND
            ));

        PropertyEntity property = propertyRepository.findById(request.getPropertyId())
            .orElseThrow(() -> new ClassificatorException(
                "Свойство не найдено",
                HttpStatus.NOT_FOUND
            ));

        if (request.getPropertyValueIds() == null || request.getPropertyValueIds().isEmpty()) {
            throw new ClassificatorException(
                "Список значений свойства пуст",
                HttpStatus.BAD_REQUEST
            );
        }

        Set<Long> requestedIds = new LinkedHashSet<>(request.getPropertyValueIds());

        List<PropertyValueEntity> values = propertyValueRepository.findAllById(requestedIds);

        if (values.size() != requestedIds.size()) {
            Set<Long> foundIds = values.stream()
                .map(PropertyValueEntity::getId)
                .collect(Collectors.toSet());

            List<Long> notFound = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

            throw new ClassificatorException(
                "Значения свойства не найдены: " + notFound,
                HttpStatus.NOT_FOUND
            );
        }

        List<Long> invalid = values.stream()
            .filter(v -> !v.getProperty().getId().equals(property.getId()))
            .map(PropertyValueEntity::getId)
            .toList();

        if (!invalid.isEmpty()) {
            throw new ClassificatorException(
                "Значения не принадлежат свойству " + property.getId() + ": " + invalid,
                HttpStatus.BAD_REQUEST
            );
        }

        List<BreedPropertyValueEntity> existing =
            breedPropertyValueRepository.findByBreedIdAndPropertyIdAndPropertyValueIdIn(
                breed.getId(),
                property.getId(),
                new ArrayList<>(requestedIds)
            );

        Set<Long> existingIds = existing.stream()
            .map(rel -> rel.getPropertyValue().getId())
            .collect(Collectors.toSet());

        if (!existing.isEmpty()) {
            breedPropertyValueRepository.deleteAll(existing);
        }

        List<Long> removed = new ArrayList<>(existingIds);

        List<Long> skipped = requestedIds.stream()
            .filter(id -> !existingIds.contains(id))
            .toList();

        return BreedPropertyValueDeleteDto.builder()
            .breedId(breed.getId())
            .propertyId(property.getId())
            .deleteValues(removed)
            .skippedValues(skipped)
            .build();
    }

}
