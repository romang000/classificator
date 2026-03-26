package org.example.kursclassificator.controller;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.dto.breedPropertyValue.*;
import org.example.kursclassificator.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BreedPropertyValueController {

    private final BreedPropertyValueService breedPropertyValueService;

    @PostMapping("/breed-property-value")
    @ResponseStatus(HttpStatus.OK)
    public BreedPropertyValueResponse addValueToBreedProperty(@RequestBody BreedPropertyValueRequest request) {
        var response = breedPropertyValueService.addValueToBreedProperty(request);
        return response;
    }

    @GetMapping("/breed-property-value")
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getByBreedAndProperty(@ModelAttribute BreedPropertyValueGetDto request) {
        var response = breedPropertyValueService.getBreedPropertyValue(request.getBreedId(), request.getPropertyId());
        return response;
    }

    @DeleteMapping("/breed-property-value")
    @ResponseStatus(HttpStatus.OK)
    public BreedPropertyValueDeleteDto delete(@RequestBody BreedPropertyValueRequest request) {
        var response = breedPropertyValueService.removeValueFromBreedProperty(request);
        return response;
    }

}
