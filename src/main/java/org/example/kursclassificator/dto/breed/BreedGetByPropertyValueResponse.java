package org.example.kursclassificator.dto.breed;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class BreedGetByPropertyValueResponse {

    List<BreedResponse> breeds;

    private String woolLength;

    private String woolColor;

    private String woolType;

    private String earType;

    private String eyeColor;

    private String eyeShape;

    private String physique;

    private String tail;

    private String paws;

}
