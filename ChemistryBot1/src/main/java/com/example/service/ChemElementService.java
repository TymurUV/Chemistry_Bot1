package com.example.service;


import com.example.dto.ChemElementDto;

import java.util.List;

public interface ChemElementService {
    ChemElementDto saveElement(ChemElementDto chemElementDto);

    List<ChemElementDto> chemicalList();

    void deleteElement(Long id);

    ChemElementDto updateElement(ChemElementDto chemElementDto, Long id);
}
