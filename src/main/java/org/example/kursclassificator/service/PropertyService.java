package org.example.kursclassificator.service;

import java.util.*;

import jakarta.transaction.*;
import lombok.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    private final PropertyValueRepository propertyValueRepository;

    private final BreedRepository breedRepository;

    public PropertyEntity create(String name) {
        var propertyAlreadyExists = propertyRepository.existsByName(name);

        if (propertyAlreadyExists) {
            throw new ClassificatorException(
                "Свойство уже существует",
                HttpStatus.CONFLICT
            );
        }

        var property = PropertyEntity.builder()
            .name(name)
            .build();

        var savedProperty = propertyRepository.save(property);
        return savedProperty;
    }

    public List<PropertyEntity> getAll() {
        var properties = propertyRepository.findAll();
        return properties;
    }

    @Transactional
    public void delete(Long id) {
        var property = propertyRepository.findById(id)
            .orElseThrow(() -> new ClassificatorException(
                "Свойство не найдено",
                HttpStatus.NOT_FOUND
            ));

        boolean hasValues = propertyValueRepository.existsByPropertyId(id);

        if (hasValues) {
            var breedNames = breedRepository.findBreedNamesByPropertyId(id);

            String message = breedNames.isEmpty()
                ? "Нельзя удалить свойство '" + property.getName() + "', потому что у него есть связанные значения"
                : "Нельзя удалить свойство '" + property.getName() + "'. Оно используется у пород: "
                  + String.join(", ", breedNames);

            throw new ClassificatorException(message, HttpStatus.CONFLICT);
        }

        propertyRepository.delete(property);
    }

}
