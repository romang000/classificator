package org.example.kursclassificator.repository;

import java.util.*;

import org.example.kursclassificator.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

@Repository
public interface BreedRepository extends JpaRepository<BreedEntity, Long> {

    boolean existsByName(String name);

    @Query("""
    select distinct b.name
    from breeds b
    join breed_properties bp on bp.breed.id = b.id
    where bp.property.id = :propertyId
""")
    List<String> findBreedNamesByPropertyId(@Param("propertyId") Long propertyId);

}
