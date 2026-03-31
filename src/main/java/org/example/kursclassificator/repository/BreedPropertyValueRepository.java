package org.example.kursclassificator.repository;

import java.util.*;

import org.example.kursclassificator.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

@Repository
public interface BreedPropertyValueRepository extends JpaRepository<BreedPropertyValueEntity, Long> {

    List<BreedPropertyValueEntity> findByBreedIdAndPropertyIdAndPropertyValueIdIn(
        Long breedId,
        Long propertyId,
        List<Long> propertyValueIds
    );

    @Query("""
        select distinct bpv.breed.name
        from breed_property_value bpv
        where bpv.propertyValue.id = :propertyValueId
    """)
    List<String> findBreedNamesByPropertyValueId(@Param("propertyValueId") Long property);

    boolean existsByBreedIdAndPropertyId(Long breedId, Long propertyId);

    boolean existsByBreedIdAndPropertyIdAndPropertyValueId(Long breedId, Long propertyId, Long valueId);

    List<BreedPropertyValueEntity> findByBreedIdAndPropertyId(
        Long breedId,
        Long propertyId
    );
}
