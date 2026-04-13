package org.example.kursclassificator.service;

import java.util.*;
import java.util.stream.*;

import jakarta.transaction.*;
import lombok.*;
import org.example.kursclassificator.dto.breed.*;
import org.example.kursclassificator.dto.property.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.mapper.*;
import org.example.kursclassificator.model.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class BreedService {

    private final BreedPropertyRepository breedPropertyRepository;

    private final BreedRepository breedRepository;

    private final BreedPropertyValueRepository breedPropertyValueRepository;

    private final BreedMapper breedMapper;

    private final PropertyRepository propertyRepository;

    private final PropertyValueRepository propertyValueRepository;

    public BreedEntity create(String name) {
        var breedAlreadyExists = breedRepository.existsByName(name);

        if (breedAlreadyExists) {
            throw new ClassificatorException(
                "Порода уже существует",
                HttpStatus.CONFLICT
            );
        }

        var breed = BreedEntity.builder()
            .name(name)
            .build();

        var savedBreed = breedRepository.save(breed);
        return savedBreed;

    }

    public List<BreedEntity> getAll() {
        var breeds = breedRepository.findAll();
        return breeds;
    }

    public BreedGetByPropertyValueResponse getByPropertyValueResponse(
        List<BreedGetByPropertyValueModel> model
    ) {
        var response = new BreedGetByPropertyValueResponse();

        if (model == null || model.isEmpty()) {
            var breeds = breedRepository.findAll().stream()
                .map(breedMapper::toDto)
                .toList();

            return response.setBreeds(breeds);
        }

        var breeds = getByPropertyValue(model).stream()
            .map(breedMapper::toDto)
            .toList();

        response.setBreeds(breeds);

        for (var filter : model) {
            var property = propertyRepository.findById(filter.getPropertyId())
                .orElse(null);

            var propertyValue = propertyValueRepository.findById(filter.getValueId())
                .orElse(null);

            if (property == null || propertyValue == null) {
                continue;
            }

            applyFeature(
                response,
                property.getName(),
                propertyValue.getValue()
            );
        }

        return response;
    }

    private List<BreedEntity> getByPropertyValue(List<BreedGetByPropertyValueModel> model) {
        if (model == null || model.isEmpty()) {
            return breedRepository.findAll();
        }

        List<BreedEntity> breeds = breedRepository.findAll();

        return breeds.stream()
            .filter(breed -> model.stream().allMatch(filter ->
                breedPropertyValueRepository.existsByBreedIdAndPropertyIdAndPropertyValueId(
                    breed.getId(),
                    filter.getPropertyId(),
                    filter.getValueId()
                )
            ))
            .toList();
    }

    private void applyFeature(
        BreedGetByPropertyValueResponse response,
        String propertyName,
        String value
    ) {
        if (propertyName == null || value == null) {
            return;
        }

        switch (propertyName.trim().toLowerCase()) {
            case "длина шерсти" -> response.setWoolLength(value);
            case "окраска шерсти" -> response.setWoolColor(value);
            case "тип шерсти" -> response.setWoolType(value);
            case "форма ушей" -> response.setEarType(value);
            case "цвет глаз" -> response.setEyeColor(value);
            case "форма глаз" -> response.setEyeShape(value);
            case "телосложение" -> response.setPhysique(value);
            case "хвост" -> response.setTail(value);
            case "лапы" -> response.setPaws(value);
            default -> {
            }
        }
    }

    public BreedCheckFillDto checkFill() {
        var breedProperties = breedPropertyRepository.findAll();

        Map<Long, List<BreedPropertyEntity>> breedPropertiesMap = breedProperties.stream()
            .collect(Collectors.groupingBy(bp -> bp.getBreed().getId()));

        List<BreedUnfilledDto> result = new ArrayList<>();

        breedPropertiesMap.forEach((breedId, properties) -> {
            String breedName = properties.getFirst().getBreed().getName();

            List<PropertyResponse> unfilledProperties = properties.stream()
                .filter(bp -> !breedPropertyValueRepository.existsByBreedIdAndPropertyId(
                    bp.getBreed().getId(),
                    bp.getProperty().getId()
                ))
                .map(bp -> PropertyResponse.builder()
                    .id(bp.getProperty().getId())
                    .name(bp.getProperty().getName())
                    .build())
                .toList();

            if (!unfilledProperties.isEmpty()) {
                result.add(BreedUnfilledDto.builder()
                    .breedId(breedId)
                    .breedName(breedName)
                    .unfilledProperties(unfilledProperties)
                    .build());
            }
        });

        return BreedCheckFillDto.builder()
            .breeds(result)
            .allFilled(result.isEmpty())
            .build();
    }

    @Transactional
    public void delete(Long id) {
        var breed = breedRepository.findById(id)
            .orElseThrow(() -> new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND
            ));

        Set<String> linkedProperties = new LinkedHashSet<>(breedPropertyRepository.findPropertyNamesByBreedId(id));

        if (!linkedProperties.isEmpty()) {
            throw new ClassificatorException(
                "Невозможно удалить породу '%s', так как с ней связаны свойства: %s"
                    .formatted(breed.getName(), String.join(", ", linkedProperties)),
                HttpStatus.BAD_REQUEST
            );
        }

        breedRepository.delete(breed);
    }

}
