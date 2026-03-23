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
@Entity(name = "breed_properties")
public class BreedPropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private BreedEntity breed;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyEntity property;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
