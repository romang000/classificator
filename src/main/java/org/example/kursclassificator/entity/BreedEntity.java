package org.example.kursclassificator.entity;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "breeds")
public class BreedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
