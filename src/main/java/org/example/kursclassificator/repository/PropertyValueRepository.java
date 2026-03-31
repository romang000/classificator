package org.example.kursclassificator.repository;

import java.util.*;

import org.example.kursclassificator.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface PropertyValueRepository extends JpaRepository<PropertyValueEntity, Long> {

    boolean existsByPropertyId(Long propertyId);

    boolean existsByPropertyIdAndValue(Long propertyId, String value);

    List<PropertyValueEntity> findByPropertyId(Long propertyId);

}
