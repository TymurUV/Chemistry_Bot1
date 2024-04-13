package com.example.mappers;

import com.example.domain.ChemElement;
import com.example.dto.ChemElementDto;
import org.springframework.stereotype.Component;

@Component
public class ChemElementMapper {
    public ChemElementDto toDto(ChemElement chemElement) {
        ChemElementDto chemElementDto = new ChemElementDto();
        chemElementDto.setId(chemElement.getId());
        chemElementDto.setName(chemElement.getName());
        chemElementDto.setSymbol(chemElement.getSymbol());
        chemElementDto.setAtomicMass(chemElement.getAtomicMass());
        chemElementDto.setAtomicNumber(chemElement.getAtomicNumber());
        return chemElementDto;
    }

    public ChemElement toEntity(ChemElementDto chemElementDto) {
        ChemElement chemElement = new ChemElement();
        chemElement.setId(chemElementDto.getId());
        chemElement.setName(chemElementDto.getName());
        chemElement.setSymbol(chemElementDto.getSymbol());
        chemElement.setAtomicMass(chemElementDto.getAtomicMass());
        chemElement.setAtomicNumber(chemElementDto.getAtomicNumber());
        return chemElement;
    }
}
