package org.example.kursclassificator.controller;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.dto.property.*;
import org.example.kursclassificator.mapper.*;
import org.example.kursclassificator.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    private final PropertyMapper propertyMapper;

    @PostMapping("/properties")
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse create(PropertyRequest property) {
        var createdProperty = propertyService.create(property.getName());
        var response = propertyMapper.toDto(createdProperty);
        return response;
    }

    @GetMapping("/properties")
    @ResponseStatus(HttpStatus.OK)
    public List<PropertyResponse> getAll() {
        var properties = propertyService.getAll();

        var response = properties.stream()
            .map(propertyMapper::toDto)
            .toList();

        return response;
    }

}
