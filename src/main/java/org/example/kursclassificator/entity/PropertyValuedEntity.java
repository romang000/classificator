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
@Entity(name = "property_values")
public class PropertyValuedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyEntity propertyId;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String code;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
