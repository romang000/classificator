package org.example.kursclassificator.service;

import java.util.*;
import java.util.stream.*;

import lombok.*;
import org.example.kursclassificator.dto.breedProperty.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class BreedPropertyService {

    private final BreedRepository breedRepository;

    private final PropertyRepository propertyRepository;

    private final BreedPropertyRepository breedPropertyRepository;

    public BreedPropertyResponse addPropertyToBreed(Long breedId, List<Long> propertiesIds) {
        BreedEntity breed = breedRepository.findById(breedId)
            .orElseThrow(() -> new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND
            ));

        List<PropertyEntity> properties = propertyRepository.findAllById(propertiesIds);

        List<BreedPropertyEntity> existingRelations =
            breedPropertyRepository.findByBreedIdAndPropertyIdIn(breedId, propertiesIds);

        Set<Long> existingPropertyIds = existingRelations.stream()
            .map(rel -> rel.getProperty().getId())
            .collect(Collectors.toSet());

        List<BreedPropertyEntity> newRelations = properties.stream()
            .filter(property -> !existingPropertyIds.contains(property.getId()))
            .map(property -> BreedPropertyEntity.builder()
                .breed(breed)
                .property(property)
                .build())
            .collect(Collectors.toList());

        if (!newRelations.isEmpty()) {
            breedPropertyRepository.saveAll(newRelations);
        }

        return BreedPropertyResponse.builder()
            .breedId(breedId)
            .addedProperties(propertiesIds)
            .skippedProperties(new ArrayList<>(existingPropertyIds))
            .build();
    }

}
