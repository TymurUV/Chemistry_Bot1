package com.example.service.impl;


import com.example.domain.ChemElement;
import com.example.dto.ChemElementDto;
import com.example.mappers.ChemElementMapper;
import com.example.mappers.UserMapper;
import com.example.repository.ChemElementRepository;
import com.example.repository.UserRepository;
import com.example.service.ChemElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChemElementImpl implements ChemElementService {
    private final ChemElementRepository chemElementRepository;
    private final ChemElementMapper chemElementMapper;


    @Autowired
    public ChemElementImpl(ChemElementRepository chemElementRepository, ChemElementMapper chemElementMapper, UserRepository userRepository, UserMapper userMapper) {
        this.chemElementRepository = chemElementRepository;
        this.chemElementMapper = chemElementMapper;
    }

    @Override
    public ChemElementDto saveElement(ChemElementDto chemElementDto) {
        ChemElement chemElement = chemElementMapper.toEntity(chemElementDto);
        return chemElementMapper.toDto(chemElementRepository.save(chemElement));
    }

    @Override
    public List<ChemElementDto> chemicalList() {
        List<ChemElement> chemElementList = chemElementRepository.findAll();
        return chemElementList.stream()
                .map(chemElementMapper::toDto)
                .toList();
    }

    @Override
    public void deleteElement(Long id) {
        chemElementRepository.deleteById(id);
    }

    @Override
    public ChemElementDto updateElement(ChemElementDto chemElementDto, Long id) {
        ChemElement chemElement = chemElementRepository.findById(id).orElse(null);
        if (chemElement == null) {
            throw new RuntimeException("Element not find by " + id + " for update");
        }

        chemElement.setName(chemElementDto.getName());
        chemElement.setSymbol(chemElementDto.getSymbol());
        chemElement.setAtomicNumber(chemElementDto.getAtomicNumber());
        chemElement.setAtomicMass(chemElementDto.getAtomicMass());

        chemElementRepository.save(chemElement);

        return chemElementMapper.toDto(chemElement);
    }
}
