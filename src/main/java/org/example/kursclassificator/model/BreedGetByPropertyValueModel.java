package org.example.kursclassificator.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedGetByPropertyValueModel {

    private Long propertyId;

    private Long valueId;

}
