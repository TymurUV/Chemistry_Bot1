package com.example.service.impl;

import com.example.domain.Scientist;
import com.example.dto.ScientistDto;
import com.example.mappers.ScientistMapper;
import com.example.repository.ScientistRepository;
import com.example.service.ScientistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScientistImpl implements ScientistService {

    private final ScientistRepository scientistRepository;

    private final ScientistMapper scientistMapper;

    @Autowired
    public ScientistImpl(ScientistRepository scientistRepository, ScientistMapper scientistMapper) {
        this.scientistRepository = scientistRepository;
        this.scientistMapper = scientistMapper;
    }

    @Override
    public ScientistDto saveScientist(ScientistDto scientistDto) {
        Scientist scientist = scientistMapper.toEntity(scientistDto);
        return scientistMapper.toDto(scientistRepository.save(scientist));
    }

    @Override
    public List<ScientistDto> scientists() {
        List<Scientist> scientists = scientistRepository.findAll();
        return scientists.stream()
                .map(scientistMapper::toDto)
                .toList();
    }

    @Override
    public void deleteScientist(Long id) {
        scientistRepository.deleteById(id);

    }

    @Override
    public ScientistDto updateScientist(ScientistDto scientistDto, Long id) {
        Scientist scientist = scientistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such scientist found"));
        scientist.setName(scientistDto.getName());
        scientist.setYearOfBirth(scientistDto.getYearOfBirth());
        scientist.setYearOfDeath(scientistDto.getYearOfDeath());
        scientist.setElementDiscovered(scientistDto.getElementDiscovered());
        scientistRepository.save(scientist);
        return scientistMapper.toDto(scientist);
    }
}
