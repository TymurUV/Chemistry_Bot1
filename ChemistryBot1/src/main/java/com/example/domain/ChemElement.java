package com.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Elements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChemElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "name")
    private String name;
    @Column(name = "atomic_number")
    private int atomicNumber;
    @Column(name = "atomic_mass")
    private double atomicMass;
}
