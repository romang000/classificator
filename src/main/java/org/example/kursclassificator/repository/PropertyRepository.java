package org.example.kursclassificator.repository;

import java.util.*;

import org.example.kursclassificator.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {

    boolean existsByName(String name);

}
