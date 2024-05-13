package com.example.mappers;

import com.example.domain.Scientist;
import com.example.dto.ScientistDto;
import org.springframework.stereotype.Component;

@Component
public class ScientistMapper {
    public ScientistDto toDto(Scientist scientist) {
        ScientistDto scientistDto = new ScientistDto();
        scientistDto.setName(scientist.getName());
        scientistDto.setYearOfBirth(scientist.getYearOfBirth());
        scientistDto.setYearOfDeath(scientist.getYearOfDeath());
        scientistDto.setElementDiscovered(scientist.getElementDiscovered());


        return scientistDto;
    }

    public Scientist toEntity(ScientistDto scientistDto) {
        Scientist scientist = new Scientist();
        scientist.setName(scientistDto.getName());
        scientist.setYearOfBirth(scientistDto.getYearOfBirth());
        scientist.setYearOfDeath(scientistDto.getYearOfDeath());
        scientist.setElementDiscovered(scientistDto.getElementDiscovered());


        return scientist;
    }
}
