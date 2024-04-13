package com.example.dto;

import lombok.Data;

@Data
public class ChemElementDto {
    private Long id;
    private String symbol;
    private String name;
    private int atomicNumber;
    private double atomicMass;
}
