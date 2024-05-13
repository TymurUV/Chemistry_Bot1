package com.example.dto;

import lombok.Data;

@Data
public class ScientistDto {
    private Long id;
    private String name;
    private int yearOfBirth;
    private int yearOfDeath;
    private String elementDiscovered;


}
