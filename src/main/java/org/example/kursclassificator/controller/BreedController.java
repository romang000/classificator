package org.example.kursclassificator.controller;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.dto.breed.*;
import org.example.kursclassificator.mapper.*;
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
    public BreedResponse createBreed(String name) {
        var breed = breedService.create(name);

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
}
