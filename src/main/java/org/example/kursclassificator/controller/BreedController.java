package org.example.kursclassificator.controller;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.dto.breed.*;
import org.example.kursclassificator.mapper.*;
import org.example.kursclassificator.model.*;
import org.example.kursclassificator.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BreedController {

    private final BreedService breedService;

    private final BreedMapper breedMapper;

    @PostMapping("/breeds")
    @ResponseStatus(HttpStatus.CREATED)
    public BreedResponse createBreed(@RequestBody BreedRequest request) {
        var breed = breedService.create(request.getName());

        var response = breedMapper.toDto(breed);
        return response;
    }

    @GetMapping("/breeds")
    @ResponseStatus(HttpStatus.OK)
    public List<BreedResponse> getAll() {
        var breeds = breedService.getAll();

        var response = breeds.stream()
            .map(breedMapper::toDto)
            .toList();

        return response;
    }

    @PostMapping("/breeds/by-property-value")
    @ResponseStatus(HttpStatus.OK)
    public BreedGetByPropertyValueResponse getBreedsByPropertyValue(
        @RequestBody List<BreedGetByPropertyValueRequest> request
    ) {
        var model = request.stream()
            .map(dto -> BreedGetByPropertyValueModel.builder()
                .propertyId(dto.getPropertyId())
                .valueId(dto.getValueId())
                .build())
            .toList();

        return breedService.getByPropertyValueResponse(model);
    }

    @PostMapping("/breeds/check-fill")
    @ResponseStatus(HttpStatus.OK)
    public BreedCheckFillDto checkFill() {
        var response = breedService.checkFill();
        return response;
    }

    @DeleteMapping("/breeds/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        breedService.delete(id);
    }

}
