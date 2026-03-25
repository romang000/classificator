package org.example.kursclassificator.service;

import java.util.*;
import java.util.stream.*;

import lombok.*;
import lombok.extern.slf4j.*;
import org.example.kursclassificator.dto.breedProperty.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.mapper.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BreedPropertyService {

    private final BreedRepository breedRepository;

    private final PropertyRepository propertyRepository;

    private final BreedPropertyRepository breedPropertyRepository;

    private final PropertyMapper propertyMapper;

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

    public BreedPropertyGetResponse getByBreedId(Long id) {
        var breedIsExists = breedRepository.existsById(id);
        if (!breedIsExists) {
            throw new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND
            );
        }

        var properties = breedPropertyRepository.findByBreedId(id);
        if (properties.isEmpty()) {
            return new BreedPropertyGetResponse()
                .setBreedId(id)
                .setProperties(List.of());
        }

        var propertyEntities = properties.stream()
            .map(BreedPropertyEntity::getProperty)
            .toList();

        var propertyDtos = propertyEntities.stream()
            .map(propertyMapper::toDto)
            .toList();

        var response = new BreedPropertyGetResponse()
            .setBreedId(id)
            .setProperties(propertyDtos);

        log.info("response for get by breed id {}", response);

        return response;
    }

    @Transactional
    public BreedPropertyResponse removePropertyFromBreed(Long breedId, List<Long> propertiesIds) {
        var breedIsExists = breedRepository.existsById(breedId);
        if (!breedIsExists) {
            throw new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND
            );
        }

        List<BreedPropertyEntity> relations =
            breedPropertyRepository.findByBreedIdAndPropertyIdIn(breedId, propertiesIds);

        Set<Long> foundPropertyIds = relations.stream()
            .map(rel -> rel.getProperty().getId())
            .collect(Collectors.toSet());

        if (!relations.isEmpty()) {
            breedPropertyRepository.deleteAll(relations);
        }

        List<Long> notFoundIds = propertiesIds.stream()
            .filter(id -> !foundPropertyIds.contains(id))
            .toList();

        return BreedPropertyResponse.builder()
            .breedId(breedId)
            .addedProperties(Collections.emptyList())
            .skippedProperties(notFoundIds)
            .build();
    }

}
