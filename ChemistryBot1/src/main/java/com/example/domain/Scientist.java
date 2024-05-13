package com.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Scientists")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scientist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "year of death")
    private int yearOfDeath;
    @Column(name = "year of birth")
    private int yearOfBirth;
    @Column(name = "Discovered Element")
    private String elementDiscovered;
}
