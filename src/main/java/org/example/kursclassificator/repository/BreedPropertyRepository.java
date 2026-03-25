package org.example.kursclassificator.repository;

import java.util.*;

import org.example.kursclassificator.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface BreedPropertyRepository extends JpaRepository<BreedPropertyEntity, Long> {

    List<BreedPropertyEntity> findByBreedIdAndPropertyIdIn(Long id, List<Long> ids);

}
