package com.example.service;

import com.example.dto.ScientistDto;

import java.util.List;

public interface ScientistService {
    ScientistDto saveScientist(ScientistDto scientistDto);

    List<ScientistDto> scientists();

    void deleteScientist(Long id);

    ScientistDto updateScientist(ScientistDto scientistDto, Long id);
}
