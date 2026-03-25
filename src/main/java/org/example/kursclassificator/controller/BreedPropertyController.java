package org.example.kursclassificator.controller;

import lombok.*;
import org.example.kursclassificator.dto.breedProperty.*;
import org.example.kursclassificator.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BreedPropertyController {

    private final BreedPropertyService breedPropertyService;

    @PostMapping("/breed-properties")
    @ResponseStatus(HttpStatus.OK)
    public BreedPropertyResponse addPropertyToBreed(@RequestBody BreedPropertyRequest request) {
        var properties = breedPropertyService.addPropertyToBreed(
            request.getBreedId(),
            request.getPropertyIds()
        );

        return properties;
    }

    @GetMapping("/breed-properties/{breedId}")
    @ResponseStatus(HttpStatus.OK)
    public BreedPropertyGetResponse getByBreedId(@PathVariable Long breedId) {
        var response = breedPropertyService.getByBreedId(breedId);
        return response;
    }

    @DeleteMapping("/breed-properties")
    @ResponseStatus(HttpStatus.OK)
    public BreedPropertyResponse deletePropertyFromBreed(@RequestBody BreedPropertyRequest request) {
        var deletedProperties = breedPropertyService.removePropertyFromBreed(
            request.getBreedId(),
            request.getPropertyIds()
        );

        return deletedProperties;
    }

}
