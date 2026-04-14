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

        List<BreedEntity> allBreeds = breedRepository.findAll();

        if (model == null || model.isEmpty()) {
            return response
                .setBreeds(allBreeds.stream().map(breedMapper::toDto).toList())
                .setRejectedBreeds(List.of());
        }

        List<BreedResponse> matchedBreeds = new ArrayList<>();
        List<BreedGetByPropertyValueRejectResponse> rejectedBreeds = new ArrayList<>();

        for (BreedEntity breed : allBreeds) {
            var rejectionReason = findRejectionReason(breed, model);

            if (rejectionReason == null) {
                matchedBreeds.add(breedMapper.toDto(breed));
            } else {
                rejectedBreeds.add(
                    new BreedGetByPropertyValueRejectResponse()
                        .setBreed(breedMapper.toDto(breed))
                        .setRejectReason(rejectionReason)
                );
            }
        }

        response.setBreeds(matchedBreeds);
        response.setRejectedBreeds(rejectedBreeds);

        for (var filter : model) {
            var property = propertyRepository.findById(filter.getPropertyId()).orElse(null);
            var propertyValue = propertyValueRepository.findById(filter.getValueId()).orElse(null);

            if (property == null || propertyValue == null) {
                continue;
            }

            applyFeature(response, property.getName(), propertyValue.getValue());
        }

        return response;
    }

    private BreedGetByPropertyValueRejectionReasonResponse findRejectionReason(
        BreedEntity breed,
        List<BreedGetByPropertyValueModel> filters
    ) {
        for (BreedGetByPropertyValueModel filter : filters) {
            var property = propertyRepository.findById(filter.getPropertyId()).orElse(null);
            var expectedValue = propertyValueRepository.findById(filter.getValueId()).orElse(null);

            if (property == null || expectedValue == null) {
                continue;
            }

            boolean matches = breedPropertyValueRepository.existsByBreedIdAndPropertyIdAndPropertyValueId(
                breed.getId(),
                filter.getPropertyId(),
                filter.getValueId()
            );

            if (matches) {
                continue;
            }

            String actualValue = findActualValueForBreedAndProperty(
                breed.getId(),
                filter.getPropertyId()
            );

            return new BreedGetByPropertyValueRejectionReasonResponse()
                .setPropertyName(property.getName())
                .setActualValue(actualValue)
                .setExpectedValue(expectedValue.getValue())
                .setReason(actualValue == null
                    ? "У породы отсутствует значение для данного свойства"
                    : "Значение свойства не совпадает");
        }

        return null;
    }

    private String findActualValueForBreedAndProperty(Long breedId, Long propertyId) {
        return breedPropertyValueRepository.findAllByBreedIdAndPropertyId(breedId, propertyId).stream()
            .map(BreedPropertyValueEntity::getPropertyValue)
            .filter(Objects::nonNull)
            .map(PropertyValueEntity::getValue)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
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
