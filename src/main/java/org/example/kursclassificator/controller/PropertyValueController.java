package org.example.kursclassificator.controller;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.dto.propertyValue.*;
import org.example.kursclassificator.mapper.*;
import org.example.kursclassificator.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PropertyValueController {

    private final PropertyValueService propertyValueService;

    private final PropertyValueMapper propertyValueMapper;

    @PostMapping("/property-value")
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyValueResponse create(@RequestBody PropertyValueRequest request) {
        var property = propertyValueService.create(
            request.getPropertyId(),
            request.getValue()
        );

        var response = propertyValueMapper.toDto(property);
        return response;
    }

    @GetMapping("/property-value")
    @ResponseStatus(HttpStatus.OK)
    public List<PropertyValueResponse> getAll() {
        var properties = propertyValueService.getAll();

        var response = properties.stream()
            .map(propertyValueMapper::toDto)
            .toList();

        return response;
    }

    @GetMapping("/property-value/{propertyId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PropertyValueResponse> getByProperty(@PathVariable Long propertyId) {
        var propertyValueByProperty = propertyValueService.getByPropertyId(propertyId);

        var response = propertyValueByProperty.stream()
            .map(propertyValueMapper::toDto)
            .toList();
        return response;
    }

    @DeleteMapping("/property-value/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        propertyValueService.delete(id);
    }

}
