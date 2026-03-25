package org.example.kursclassificator.service;

import java.util.*;

import lombok.*;
import org.example.kursclassificator.entity.*;
import org.example.kursclassificator.exception.*;
import org.example.kursclassificator.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class BreedService {

    private final BreedRepository breedRepository;

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

    public void delete(Long id) {
        var breed = breedRepository.findById(id)
            .orElseThrow(() -> new ClassificatorException(
                "Порода не найдена",
                HttpStatus.NOT_FOUND)
            );
        breedRepository.delete(breed);
    }

}
