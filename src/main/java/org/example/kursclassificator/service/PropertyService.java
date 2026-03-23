package org.example.kursclassificator.service;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.repository.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyEntity create(String name) {
        var propertyAlreadyExists = propertyRepository.existsByName(name);

        if (propertyAlreadyExists) {
            throw new RuntimeException("already exists");
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

}
