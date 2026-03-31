package org.example.kursclassificator.repository;

import java.util.*;

import org.example.kursclassificator.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

@Repository
public interface BreedPropertyRepository extends JpaRepository<BreedPropertyEntity, Long> {

    @Query("""
        select distinct bp.property.name
        from breed_properties bp
        where bp.breed.id = :breedId
    """)
    List<String> findPropertyNamesByBreedId(@Param("breedId") Long breedId);

    List<BreedPropertyEntity> findByBreedIdAndPropertyIdIn(Long id, List<Long> ids);

    List<BreedPropertyEntity> findByBreedId(Long id);

}
