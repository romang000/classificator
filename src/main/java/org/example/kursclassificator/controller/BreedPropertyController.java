package org.example.kursclassificator.controller;

import java.util.*;

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

}
